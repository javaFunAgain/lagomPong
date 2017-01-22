package pl.setblack.pongi.sessions.impl;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.HashMap;
import jdk.nashorn.internal.ir.annotations.Immutable;
import pl.setblack.pongi.users.Session;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by jarek on 1/15/17.
 */
@Immutable
public class Sessions {
    private final HashMap<UUID, Session> activeSessions;

    private final Clock clock;

    Sessions(final Clock clock, final HashMap<UUID, Session> activeSessions) {
        this.clock = clock;
        this.activeSessions = activeSessions;
    }

    public Sessions() {
        this(Clock.systemUTC(), HashMap.empty());
    }

    Session createSession(final String userId) {
        final LocalDateTime now = LocalDateTime.now(this.clock);
        final LocalDateTime expirationTime = now.plusDays(1);
        final UUID sessionUUID = UUID.randomUUID();

       return new Session(userId, sessionUUID, expirationTime);
    }


    Sessions withNewSession( final Session ses) {
        return new Sessions(this.clock, this.activeSessions.put(ses.uuid, ses));
    }
}
