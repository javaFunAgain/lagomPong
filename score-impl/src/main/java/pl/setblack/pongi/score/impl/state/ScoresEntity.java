package pl.setblack.pongi.score.impl.state;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import javaslang.control.Option;

import java.util.Optional;

/**
 * Created by jarek on 3/21/17.
 */
public class ScoresEntity extends PersistentEntity<ScoreCommand, ScoreEvent, ScoresState> {
    @Override
    public Behavior initialBehavior(Optional<ScoresState> snapshotState) {
        final BehaviorBuilder b = newBehaviorBuilder(
                snapshotState.orElse(new ScoresState()));

        b.setEventHandler(ScoreEvent.RecordsAdded.class,
                event -> state().registerScores(event.records));

        b.setCommandHandler(ScoreCommand.RegisterRecords.class,
                (cmd, ctx) -> ctx.thenPersist(new ScoreEvent.RecordsAdded(cmd.records), ev -> ctx.reply(Done.getInstance()))
        );

        return b.build();
    }
}
