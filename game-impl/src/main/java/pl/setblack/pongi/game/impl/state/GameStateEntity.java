package pl.setblack.pongi.game.impl.state;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import javaslang.control.Option;


import java.util.Optional;

/**
 * Created by jarek on 1/23/17.
 */
public class GameStateEntity extends PersistentEntity<GameStateCommand, GameStateEvent, InternalGameState> {
    @Override
    public Behavior initialBehavior(Optional<InternalGameState> snapshotState) {
        final BehaviorBuilder b = newBehaviorBuilder(
                snapshotState.orElse(new InternalGameState(Option.none())));


        b.setEventHandler(GameStateEvent.GameStarted.class,
                event -> state().startFrom (event.info, event.startTime));
        b.setEventHandler(GameStateEvent.GameCycle.class,
                event -> state().push (event.time));

        b.setEventHandler(GameStateEvent.PlayerMovingTo.class,
                event -> state().withPlayerMovingTo(event.userId, event.targetY));

        b.setCommandHandler(GameStateCommand.StartGame.class,
                (cmd, ctx)->
                        ctx.thenPersist( new GameStateEvent.GameStarted(cmd.gameInfo, cmd.startTime),
                               ev -> ctx.reply(state().game) )
                );

        b.setCommandHandler(GameStateCommand.PushGameLoop.class,
                (cmd, ctx)->
                        ctx.thenPersist( new GameStateEvent.GameCycle(cmd.time),
                                ev -> ctx.reply(state().game) )
        );

        b.setCommandHandler(GameStateCommand.MovePlayerPaddle.class,
                (cmd, ctx)->
                        ctx.thenPersist( new GameStateEvent.PlayerMovingTo(cmd.userId,cmd.targetY),
                                ev -> ctx.reply(Done.getInstance()))
        );


        b.setReadOnlyCommandHandler(GameStateCommand.GetGame.class,
                (cmd,ctx) -> ctx.reply(state().game));


        return b.build();
    }
}
