package pl.setblack.pongi.game.impl.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javaslang.Tuple;
import javaslang.control.Option;
import pl.setblack.pongi.game.impl.*;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Random;

/**
 * Created by jarek on 1/23/17.
 */
@JsonDeserialize
@Immutable
public class InternalGameState implements Serializable {
    public final Option<GameState> game;

    private final Random random = new Random(7);

    @JsonCreator
    public InternalGameState(Option<GameState> game) {
        this.game = game;
    }

    public InternalGameState startFrom(GameInfo info, long  startTime) {
        if ( game.isDefined()) {
            return this;
        }
        if ( info.players.size()==2) {
            final Option<GameState> state = GameState.startFrom(info, startTime, random);
            return new InternalGameState(state);

        } else {
            return new InternalGameState(Option.none());
        }
    }

    private static Paddle createPaddle(int playerNr) {
        final float x = (float)playerNr -1;
        return  new Paddle(x, 0.5f);
    }

    public InternalGameState push(long time) {
        return new InternalGameState(this.game.map( state->state.push(time, random)));
    }

    public InternalGameState withPlayerMovingTo(String userId, float targetY) {
        return new InternalGameState( this.game.map( gm -> gm.playerMovingTo(userId, targetY)));
    }
}
