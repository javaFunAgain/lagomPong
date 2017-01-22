package pl.setblack.pongi.game.impl.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import javaslang.collection.List;
import javaslang.control.Option;
import pl.setblack.pongi.game.impl.GameInfo;

import javax.annotation.concurrent.Immutable;

/**
 * Created by jarek on 1/22/17.
 */
public class GamesInfoEvent {

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    class GetList implements GamesInfoCommand, CompressedJsonable, PersistentEntity.ReplyType<List<GameInfo>> {


    }
}
