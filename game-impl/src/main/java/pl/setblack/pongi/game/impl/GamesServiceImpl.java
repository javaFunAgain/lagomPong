package pl.setblack.pongi.game.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import javaslang.collection.List;
import pl.setblack.pongi.game.impl.info.GamesInfo;
import pl.setblack.pongi.game.impl.info.GamesInfoEntity;
import scala.Unit;

import javax.inject.Inject;


public class GamesServiceImpl implements GamesService {

    private final PersistentEntityRegistry persistentEntityRegistry;

    @Inject
    public GamesServiceImpl(PersistentEntityRegistry persistentEntityRegistry) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        this.persistentEntityRegistry.register(GamesInfoEntity.class);
    }

    @Override
    public ServiceCall<NotUsed, List<GameInfo>> games() {
        return null;
    }

    @Override
    public ServiceCall<String, GameInfo> create() {
        return null;
    }

    @Override
    public ServiceCall<String, GameState> join() {
        return null;
    }
}
