package pl.setblack.pongi.game.impl.info;

import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.util.Optional;

/**
 * Created by jarek on 1/22/17.
 */
public class GamesInfoEntity extends PersistentEntity<GamesInfoCommand, GamesInfoEvent, GamesInfo> {
    @Override
    public Behavior initialBehavior(Optional<GamesInfo> snapshotState) {
        final BehaviorBuilder b = newBehaviorBuilder(
                snapshotState.orElse(new GamesInfo()));

        return  b.build();
    }
}
