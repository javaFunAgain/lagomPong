package pl.setblack.pongi.sessions.impl;

import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import javaslang.control.Option;
import pl.setblack.pongi.users.Session;

import java.util.Optional;

/**
 * Created by jarek on 1/15/17.
 */
public class SessionsEntity extends PersistentEntity<SessionsCommand, SessionsEvent, Sessions> {
    @Override
    public Behavior initialBehavior(Optional<Sessions> snapshotState) {
        final BehaviorBuilder b = newBehaviorBuilder(
                snapshotState.orElse(new Sessions()));

        b.setEventHandler(SessionsEvent.SessionAdded.class, this::startSession);

        b.setCommandHandler(SessionsCommand.StartSession.class,
                (cmd, ctx)-> {
                        final Session ses = state().createSession(cmd.userId);
                       return ctx.thenPersist(new SessionsEvent.SessionAdded(ses),
                               evt-> ctx.reply(Option.some(ses)));
                });

        return b.build();
    }

    private Sessions  startSession(final SessionsEvent.SessionAdded evt) {
        return state().withNewSession(evt.session);
    }
}
