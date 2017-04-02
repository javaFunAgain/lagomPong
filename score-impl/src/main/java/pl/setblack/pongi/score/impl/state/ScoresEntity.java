package pl.setblack.pongi.score.impl.state;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.util.Optional;

/**
 * Created by jarek on 3/21/17.
 */
public class ScoresEntity extends PersistentEntity<ScoreCommand, ScoreEvent, ScoreState> {
    @Override
    public Behavior initialBehavior(Optional<ScoreState> snapshotState) {
        final BehaviorBuilder b = newBehaviorBuilder(
                snapshotState.orElse(new ScoreState()));

        b.setEventHandler(ScoreEvent.RecordAdded.class,
                event -> state().registerSingleRecord(event.record));

        b.setCommandHandler(ScoreCommand.RegisterRecord.class,
                (cmd, ctx) -> ctx.thenPersist(new ScoreEvent.RecordAdded(cmd.record), ev -> ctx.reply(Done.getInstance()))
        );

        return b.build();
    }
}
