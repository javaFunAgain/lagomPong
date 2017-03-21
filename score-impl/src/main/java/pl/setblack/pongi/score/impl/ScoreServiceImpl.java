package pl.setblack.pongi.score.impl;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import javaslang.collection.List;
import pl.setblack.pongi.score.ScoreRecord;
import pl.setblack.pongi.score.ScoreService;
import pl.setblack.pongi.score.UserScore;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Created by jarek on 3/21/17.
 */
public class ScoreServiceImpl implements ScoreService{

    private final PersistentEntityRegistry persistentEntityRegistry;

    @Inject
    public ScoreServiceImpl(PersistentEntityRegistry persistentEntityRegistry) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        //this.persistentEntityRegistry.register(GameStateEntity.class);
    }


    @Override
    public ServiceCall<NotUsed, List<UserScore>> getTopScores() {
        return request -> CompletableFuture.completedFuture( List.empty());
    }

    @Override
    public ServiceCall<List<ScoreRecord>, Done> registerScore() {
        return request -> CompletableFuture.completedFuture( Done.getInstance());
    }
}
