package pl.setblack.pongi.game.impl.state;

import javaslang.Tuple;
import javaslang.control.Option;
import pl.setblack.pongi.game.impl.*;

import javax.annotation.concurrent.Immutable;

/**
 * Created by jarek on 1/23/17.
 */
@Immutable
public class InternalGameState {
    public final Option<GameState> game;

    public InternalGameState(Option<GameState> game) {
        this.game = game;
    }

    public static InternalGameState startFrom(GameInfo info) {
        final Ball ball = new Ball(0.5f, 0.5f);
        final Player player1 = new Player(0, info.players.get(0), createPaddle(1));
        final Player player2 = new Player(0, info.players.get(1), createPaddle(2));
        final GameState state = new GameState(ball, Tuple.of(player1,player2));
        return new InternalGameState(Option.some(state));
    }

    private static Paddle createPaddle(int playerNr) {
        final float x = (float)playerNr -1;
        return  new Paddle(x, 0.5f);
    }
}
