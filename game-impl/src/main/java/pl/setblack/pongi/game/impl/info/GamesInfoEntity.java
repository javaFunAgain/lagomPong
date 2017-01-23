package pl.setblack.pongi.game.impl.info;

import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import pl.setblack.pongi.game.impl.GameInfo;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by jarek on 1/22/17.
 */
public class GamesInfoEntity extends PersistentEntity<GamesInfoCommand, GamesInfoEvent, GamesInfo> {
    @Override
    public Behavior initialBehavior(Optional<GamesInfo> snapshotState) {
        final BehaviorBuilder b = newBehaviorBuilder(
                snapshotState.orElse(new GamesInfo()));

        b.setEventHandler(GamesInfoEvent.GameCreated.class,
                evt -> state().withGame(evt.game));

        b.setEventHandler(GamesInfoEvent.GameJoined.class,
                evt -> state().joinGame(evt.gameId, evt.userId));

        b.setCommandHandler( GamesInfoCommand.Create.class,
                (cmd,ctx) -> {
                    final UUID uuid = UUID.randomUUID();
                    final GameInfo info = new GameInfo(
                            cmd.name,
                            uuid.toString(),
                            cmd.userId);
                    return ctx.thenPersist(new GamesInfoEvent.GameCreated(info),
                            ev -> ctx.reply(info));
                });

        b.setReadOnlyCommandHandler( GamesInfoCommand.GetList.class,
                (cmd,ctx) ->
                        ctx.reply( state()
                                .allGames)

        );

        b.setCommandHandler( GamesInfoCommand.JoinGame.class,
                (cmd,ctx) ->
                        ctx.thenPersist(new GamesInfoEvent.GameJoined(cmd.gameId, cmd.userId),
                                ev -> ctx.reply( state().findGame(cmd.gameId)))

        );

        return  b.build();
    }
}
