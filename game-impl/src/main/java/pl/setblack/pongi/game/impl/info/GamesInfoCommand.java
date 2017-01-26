package pl.setblack.pongi.game.impl.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import javaslang.collection.List;
import javaslang.control.Option;
import pl.setblack.pongi.game.impl.GameInfo;

import javax.annotation.concurrent.Immutable;

/**
 * Created by jarek on 1/22/17.
 */
public interface GamesInfoCommand extends Jsonable {

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    class GetList implements GamesInfoCommand , CompressedJsonable, PersistentEntity.ReplyType<List<GameInfo>>{

    }

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    class JoinGame implements GamesInfoCommand , CompressedJsonable, PersistentEntity.ReplyType<Option<GameInfo>>{
        public final String gameId;
        public final String userId;
        @JsonCreator
        public JoinGame(String gameId, String userId) {
            this.gameId = gameId;
            this.userId = userId;
        }
    }

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    class Create implements GamesInfoCommand , CompressedJsonable, PersistentEntity.ReplyType<GameInfo>{
        public final String  name;
        public final String userId;

        @JsonCreator
        public Create(String name, String userId) {

            this.name = name;
            this.userId = userId;
        }
    }
}
