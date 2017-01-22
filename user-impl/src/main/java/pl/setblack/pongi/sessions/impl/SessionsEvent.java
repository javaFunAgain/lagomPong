package pl.setblack.pongi.sessions.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.Jsonable;
import pl.setblack.pongi.users.Session;

import javax.annotation.concurrent.Immutable;

/**
 * Created by jarek on 1/15/17.
 */
public interface SessionsEvent extends Jsonable {
    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    class SessionAdded implements SessionsEvent {
        public final Session session;

        @JsonCreator
        public SessionAdded(Session session) {
            this.session = session;
        }
    }
}
