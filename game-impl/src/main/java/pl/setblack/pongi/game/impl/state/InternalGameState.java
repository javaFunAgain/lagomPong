package pl.setblack.pongi.game.impl.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javaslang.Tuple;
import javaslang.control.Option;
import pl.setblack.pongi.game.impl.*;

import javax.annotation.concurrent.Immutable;

/**
 * Created by jarek on 1/23/17.
 */
@JsonDeserialize
@Immutable
public class InternalGameState {
    public final Option<GameState> game;

    @JsonCreator
    public InternalGameState(Option<GameState> game) {
        this.game = game;
    }

    public InternalGameState startFrom(GameInfo info, long  startTime) {
        if ( game.isDefined()) {
            return this;
        }
        if ( info.players.size()==2) {
            final Ball ball = new Ball(0.5f, 0.5f);
            final Player player1 = new Player(0, info.players.get(0), createPaddle(1));
            final Player player2 = new Player(0, info.players.get(1), createPaddle(2));
            final GameState state = new GameState(ball, Tuple.of(player1,player2), startTime);
            return new InternalGameState(Option.some(state.start(startTime)));

        } else {
            return new InternalGameState(Option.none());
        }
    }

    private static Paddle createPaddle(int playerNr) {
        final float x = (float)playerNr -1;
        return  new Paddle(x, 0.5f);
    }

    public InternalGameState push(long time) {
        return new InternalGameState(this.game.map( state->state.push(time)));
    }

    public InternalGameState withPlayerMovingTo(String userId, float targetY) {
        return new InternalGameState( this.game.map( gm -> gm.playerMovingTo(userId, targetY)));
    }
}
