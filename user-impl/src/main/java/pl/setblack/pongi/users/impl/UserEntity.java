package pl.setblack.pongi.users.impl;

import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import javaslang.control.Option;
import pl.setblack.pongi.users.RegUserStatus;

import java.util.Optional;

/**
 * Created by jarek on 1/14/17.
 */
public class UserEntity extends PersistentEntity<UserCommand, UserEvent, UserState> {
    @Override
    public Behavior initialBehavior(Optional<UserState> snapshotState) {
        BehaviorBuilder b = newBehaviorBuilder(
                snapshotState.orElse(UserState.empty()));

        b.setEventHandler(UserEvent.RegisteredUser.class,
                evt -> {
                        System.out.println("event ... new user:"+ evt.userId);
                    System.out.println("and before was:"+ state().user);

                        return new UserState(Option.some(new UserData(evt.userId, evt.data.password)));
                });

        b.setCommandHandler(UserCommand.RegisterUser.class, this::handleRegisterUserCMD);

        b.setReadOnlyCommandHandler( UserCommand.LoginUser.class,
                (cmd,ctx) ->
                   ctx.reply( state()
                                    .user.map(
                                            u-> u.hashedPassword
                                                    .equals(cmd.data.password))
                                    .getOrElse(false))

                );

        return b.build();
    }

    private Persist handleRegisterUserCMD(UserCommand.RegisterUser cmd, CommandContext ctx) {
        if (state().user.isDefined()) {
            ctx.reply(new RegUserStatus(Option.some("user exists")));
            return ctx.done();
        } else {
            return ctx.thenPersist(new UserEvent.RegisteredUser(cmd.userId, cmd.data),
                    evt -> ctx.reply( new RegUserStatus(Option.none())));
        }
    }
}
