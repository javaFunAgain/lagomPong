package pl.setblack.pongi.score.impl.state;

import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import javaslang.collection.List;
import pl.setblack.pongi.score.ScoreRecord;
import pl.setblack.pongi.score.UserScore;

import javax.annotation.concurrent.Immutable;

/**
 * Created by jarek on 3/21/17.
 */
public interface ScoreCommand extends Jsonable {

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    final class RegisterRecord implements
            ScoreCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {
        final ScoreRecord record;

        @JsonCreator
        public RegisterRecord(ScoreRecord record) {
            this.record = record;
        }
    }




}
