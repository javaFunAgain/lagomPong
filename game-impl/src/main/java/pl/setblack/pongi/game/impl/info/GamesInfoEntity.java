package pl.setblack.pongi.game.impl.info;

import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import javaslang.collection.List;
import pl.setblack.pongi.game.impl.GameInfo;
import pl.setblack.pongi.game.impl.Player;
import pl.setblack.pongi.score.GameResult;
import pl.setblack.pongi.score.ScoreRecord;

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

        b.setEventHandler(GamesInfoEvent.GameRemoved.class,
                evt -> state().removeGame(evt.gameId));


        b.setCommandHandler(GamesInfoCommand.EndGame.class,
                (cmd, ctx) ->
                        state().findGame(cmd.gameId).map(
                                gameFound ->
                                        ctx.thenPersist(new GamesInfoEvent.GameRemoved(cmd.gameId),
                                                evt -> ctx.reply(List.of(
                                                        createScore(cmd.state.players._1, cmd.state.players._2, cmd.gameId),
                                                        createScore(cmd.state.players._2, cmd.state.players._1, cmd.gameId)

                                                )))


                        ).getOrElse(() -> ctx.thenPersistAll(List.<GamesInfoEvent.GameRemoved>empty().toJavaList(), () -> ctx.reply(List.empty())))

        );

        b.setCommandHandler(GamesInfoCommand.Create.class,
                (cmd, ctx) -> {
                    final UUID uuid = UUID.randomUUID();
                    final GameInfo info = new GameInfo(
                            cmd.name,
                            uuid.toString(),
                            cmd.userId);
                    return ctx.thenPersist(new GamesInfoEvent.GameCreated(info),
                            ev -> ctx.reply(info));
                });

        b.setReadOnlyCommandHandler(GamesInfoCommand.GetList.class,
                (cmd, ctx) ->
                        ctx.reply(state()
                                .allGames)

        );

        b.setCommandHandler(GamesInfoCommand.JoinGame.class,
                (cmd, ctx) ->
                        ctx.thenPersist(new GamesInfoEvent.GameJoined(cmd.gameId, cmd.userId),
                                ev -> ctx.reply(state().findGame(cmd.gameId)))

        );

        return b.build();
    }

    private ScoreRecord createScore(final Player player, final Player opponent, final String gameId) {
        return new ScoreRecord(
                player.name,
                calcResult(player, opponent),
                player.score,
                opponent.score,
                gameId
        );
    }

    private GameResult calcResult(final Player player, final Player opponent) {
        if (player.score > opponent.score) {
            return GameResult.WON;
        }
        if (player.score < opponent.score) {
            return GameResult.LOST;
        }
        throw new IllegalStateException();
    }
}
