package pl.setblack.pongi.game.impl.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.Jsonable;
import pl.setblack.pongi.game.impl.GameInfo;

import javax.annotation.concurrent.Immutable;

/**
 * Created by jarek on 1/23/17.
 */
public interface GameStateEvent extends Jsonable {

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    final class GameStarted implements GameStateEvent {
        public final GameInfo info;

        public GameStarted(GameInfo info) {
            this.info = info;
        }
    }

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    final class GameCycle implements GameStateEvent {
        final long time;
        @JsonCreator
        public GameCycle(long time) {
            this.time = time;
        }

    }

}
