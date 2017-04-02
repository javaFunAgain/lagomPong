package pl.setblack.pongi.score.impl.state;

import akka.Done;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import javaslang.Value;
import javaslang.concurrent.Future;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import pl.setblack.pongi.score.GameResult;
import pl.setblack.pongi.score.ScoreRecord;
import pl.setblack.pongi.score.UserScore;
import pl.setblack.pongi.score.impl.state.ScoreEvent;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSide.*;
/**
 * Created by jarek on 3/28/17.
 */
public class ScoreRSProcessor extends ReadSideProcessor<ScoreEvent> {



    private final CassandraSession session;
    private final CassandraReadSide readSide;

    private PreparedStatement writeScore = null;

    @Inject
    public ScoreRSProcessor(CassandraSession session, CassandraReadSide readSide) {
        this.session = session;
        this.readSide = readSide;


    }


    @Override
    public ReadSideHandler<ScoreEvent> buildHandler() {
        CassandraReadSide.ReadSideHandlerBuilder<ScoreEvent> builder =
                readSide.<ScoreEvent>builder("scores")
                .setGlobalPrepare(this::createTable)
                .setPrepare(tag -> prepareWriteScore())
                .setEventHandler(ScoreEvent.RecordAdded.class, this::processScoreAdded);
        return builder.build();
    }



    @Override
    public PSequence<AggregateEventTag<ScoreEvent>> aggregateTags() {
        return TreePVector.singleton(ScoreEvent.SCORE_EVENT_TAG);
    }

    private CompletionStage<List<BoundStatement>> processScoreAdded(ScoreEvent.RecordAdded event) {
        System.out.println("score in PROCESSOR:"+ event);
        final ScoreRecord singleRecord =  event.record;
          return this.getSingleScore(singleRecord.userId).thenApply(
                    score -> {
                        BoundStatement bindWriteScore = writeScore.bind();
                        boolean won = singleRecord.result == GameResult.WON;
                        bindWriteScore.setInt("totalScore", score.totalScore  +  (won ? 5 : 0 ));
                        bindWriteScore.setInt("gamesWon", score.gamesWon + (won ? 1 : 0));
                        bindWriteScore.setInt("gamesLost",score.gamesLost + (won ? 0 : 1) );
                        bindWriteScore.setInt("gamesPlayed",score.gamesPlayed + 1 );
                        bindWriteScore.setInt("pointsScored", score.pointsScored+ singleRecord.playerScored);
                        bindWriteScore.setInt("pointsLost", score.pointsLost+ singleRecord.opponentScore);
                        bindWriteScore.setString("userId", singleRecord.userId);
                        System.out.println("bound for:"+singleRecord.userId);
                        return bindWriteScore;
                    }
            ).thenApply(bs -> Arrays.asList(bs));
    }


    private CompletionStage<UserScore> getSingleScore(String userId) {
        return session.selectOne("SELECT userId, " +
                "totalScore, " +
                "gamesWon, " +
                "gamesLost, " +
                "gamesPlayed, " +
                "pointsScored, " +
                "pointsLost FROM userScore WHERE userId = ?;",userId )
                .thenApply( optRow-> optRow.map(getRowUserScoreFunction()).orElseGet(()->UserScore.emptyFor(userId)) );
    }

    private CompletionStage<Done> createTable() {
        return session.executeCreateTable("CREATE TABLE IF NOT EXISTS userScore ( " +
                "userId TEXT, " +
                "totalScore INT, " +
                "gamesWon INT, " +
                "gamesLost INT, " +
                "gamesPlayed  INT, " +
                "pointsScored INT,  " +
                "pointsLost INT,  " +
                "PRIMARY KEY (userId))");
    }

    private CompletionStage<Done> prepareWriteScore() {
        return session.prepare(
                "UPDATE userScore" +
                " SET totalScore = :totalScore,  " +
                        "gamesWon = :gamesWon, " +
                        "gamesLost =  :gamesLost, " +
                        "gamesPlayed = :gamesPlayed , " +
                        "pointsScored =  :pointsScored, " +
                        "pointsLost =  :pointsLost " +
                "WHERE userId = ?")
                .thenApply(ps -> {
                    this.writeScore = ps;
                    return Done.getInstance();
                });



    }

    public static Function<Row, UserScore> getRowUserScoreFunction() {
        return row ->
                new UserScore(
                        row.getString("userId"),
                        row.getInt("totalScore"),
                        row.getInt("gamesWon"),
                        row.getInt("gamesLost"),
                        row.getInt("gamesPlayed"),
                        row.getInt("pointsScored"),
                        row.getInt("pointsLost")
                );
    }
}
