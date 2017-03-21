package pl.setblack.pongi.score.impl.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javaslang.collection.HashMap;
import javaslang.collection.List;
import javaslang.collection.PriorityQueue;
import pl.setblack.pongi.score.ScoreRecord;
import pl.setblack.pongi.score.ScoreService;
import pl.setblack.pongi.score.UserScore;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by jarek on 3/21/17.
 */
@JsonDeserialize
@Immutable
public class ScoresState implements Serializable{
    private final HashMap<String, UserScore> scores;

    @JsonCreator
    public ScoresState(final HashMap<String, UserScore> scores) {
        this.scores = scores;
    }


    ScoresState() {
        this( HashMap.empty());
    }

    public ScoresState registerScores(List<ScoreRecord> records) {
        return records.foldLeft(this, (state, rec) -> state.registerSingleRecord(rec));
    }

    private ScoresState registerSingleRecord(ScoreRecord record) {
        final UserScore score = this.scores.get(record.userId).getOrElse(
                () ->  UserScore.emptyFor(record.userId)
        );

        return new ScoresState( this.scores.put(record.userId, score));
    }



    private static final class UserScoreComparator implements Comparator<UserScore> {
        @Override
        public int compare(UserScore o1, UserScore o2) {
            return o2.pointsScored - o1.pointsScored;
        }
    }
}
