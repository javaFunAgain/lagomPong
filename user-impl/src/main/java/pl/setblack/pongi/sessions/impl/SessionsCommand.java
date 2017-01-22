package pl.setblack.pongi.sessions.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import javaslang.control.Option;
import pl.setblack.pongi.users.Session;

import javax.annotation.concurrent.Immutable;

/**
 * Created by jarek on 1/15/17.
 */
public interface SessionsCommand extends Jsonable {
    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    class StartSession implements SessionsCommand, CompressedJsonable, PersistentEntity.ReplyType<Option<Session>> {
        public final String userId;

        @JsonCreator
        public StartSession(String userId) {
            this.userId = userId;
        }
    }
}
