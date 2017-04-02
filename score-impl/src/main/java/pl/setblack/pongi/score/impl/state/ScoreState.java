package pl.setblack.pongi.score.impl.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javaslang.collection.HashMap;
import javaslang.collection.List;
import javaslang.control.Option;
import pl.setblack.pongi.score.ScoreRecord;
import pl.setblack.pongi.score.UserScore;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by jarek on 3/21/17.
 */
@JsonDeserialize
@Immutable
public class ScoreState implements Serializable{
    private final Option<UserScore> score;

    @JsonCreator
    public ScoreState(final Option<UserScore> score) {
        this.score = score;
    }


    ScoreState() {
        this( Option.none());
    }


     ScoreState registerSingleRecord(ScoreRecord record) {
        final UserScore score = this.score.getOrElse(
                () ->  UserScore.emptyFor(record.userId)
        ).add(record);

        return new ScoreState( Option.of(score));
    }

}
