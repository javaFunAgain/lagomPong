package pl.setblack.pongi.score.impl.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.Jsonable;
import javaslang.collection.List;
import pl.setblack.pongi.score.ScoreRecord;

import javax.annotation.concurrent.Immutable;

/**
 * Created by jarek on 3/21/17.
 */
public interface ScoreEvent extends Jsonable {

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    final class RecordsAdded implements ScoreEvent {
        final List<ScoreRecord> records;

        @JsonCreator
        public RecordsAdded(List<ScoreRecord> records) {
            this.records = records;
        }
    }
}
