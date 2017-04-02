package pl.setblack.pongi.score.impl;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import javaslang.collection.List;
import javaslang.concurrent.Future;
import pl.setblack.pongi.score.ScoreRecord;
import pl.setblack.pongi.score.ScoreService;
import pl.setblack.pongi.score.UserScore;
import pl.setblack.pongi.score.impl.state.ScoreCommand;
import pl.setblack.pongi.score.impl.state.ScoreRSProcessor;
import pl.setblack.pongi.score.impl.state.ScoresEntity;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Created by jarek on 3/21/17.
 */
public class ScoreServiceImpl implements ScoreService {

    private final PersistentEntityRegistry persistentEntityRegistry;

    private final CassandraSession cassandraSession;

    @Inject
    public ScoreServiceImpl(PersistentEntityRegistry persistentEntityRegistry,
                            CassandraSession cassandraSession,
                            final ReadSide readSide) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        this.cassandraSession = cassandraSession;
        this.persistentEntityRegistry.register(ScoresEntity.class);
        readSide.register(ScoreRSProcessor.class);
    }


    @Override
    public ServiceCall<List<ScoreRecord>, Done> registerScore() {
        return request -> {
        final CompletableFuture<Done> result = new CompletableFuture<>();
            Future.sequence(
                    request.map(scoreRecord -> {
                        final PersistentEntityRef<ScoreCommand> ref = persistentEntityRegistry.refFor(ScoresEntity.class, scoreRecord.userId);
                        return ref.ask(new ScoreCommand.RegisterRecord(scoreRecord));
                    })
                            .map(cs -> Future.fromJavaFuture(cs.toCompletableFuture())))
                    .map(whatever -> Done.getInstance())
                    .onComplete(whatever -> result.complete(Done.getInstance()));
            return result;
        };


    }

    @Override
    public ServiceCall<NotUsed, List<UserScore>> getTopScores() {
        return request -> {
            return cassandraSession.selectAll(
                    "SELECT userId, " +
                            "totalScore, " +
                            "gamesWon, " +
                            "gamesLost, " +
                            "gamesPlayed, " +
                            "pointsScored, " +
                            "pointsLost FROM userScore " +
                            "ORDER BY totalScore DESC" +
                            " LIMIT 20;")
                    .thenApply(allRows ->
                            List.ofAll(allRows)
                                    .map(ScoreRSProcessor.getRowUserScoreFunction())

                    );

        };
    }


}
