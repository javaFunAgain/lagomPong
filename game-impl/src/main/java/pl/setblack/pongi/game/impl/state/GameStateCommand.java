package pl.setblack.pongi.game.impl.state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import javaslang.control.Option;
import pl.setblack.pongi.game.impl.GameInfo;
import pl.setblack.pongi.game.impl.GameState;

import javax.annotation.concurrent.Immutable;

/**
 * Created by jarek on 1/23/17.
 */
public interface GameStateCommand extends Jsonable {

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    class StartGame implements GameStateCommand, CompressedJsonable, PersistentEntity.ReplyType<Option<GameState>> {
        public final GameInfo gameInfo;

        @JsonCreator
        public StartGame(GameInfo gameInfo) {
            this.gameInfo = gameInfo;
        }
    }

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    class PushGameLoop implements GameStateCommand, CompressedJsonable, PersistentEntity.ReplyType<GameState> {
        public final long time;
        public PushGameLoop(long time) {
            this.time = time;
        }



    }
}
