package pl.setblack.pongi.game.impl.state;

import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import javaslang.control.Option;
import pl.setblack.pongi.game.impl.GameState;


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
                event -> InternalGameState.startFrom (event.info));

        b.setCommandHandler(GameStateCommand.StartGame.class,
                (cmd, ctx)->
                        ctx.thenPersist( new GameStateEvent.GameStarted(cmd.gameInfo),
                               ev -> ctx.reply(state().game) )
                );

        return b.build();
    }
}
