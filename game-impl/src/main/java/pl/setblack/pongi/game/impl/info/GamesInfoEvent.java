package pl.setblack.pongi.game.impl.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.Jsonable;
import pl.setblack.pongi.game.impl.GameInfo;

import javax.annotation.concurrent.Immutable;

public interface GamesInfoEvent extends Jsonable {

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    final class GameCreated implements  GamesInfoEvent {
        public final GameInfo game;

        @JsonCreator
        public GameCreated(GameInfo game) {

            this.game = game;
        }
    }

}
