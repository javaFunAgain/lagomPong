package pl.setblack.pongi.score;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import javaslang.collection.List;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

/**
 * Created by jarek on 3/20/17.
 */
public interface ScoreService extends Service {

    ServiceCall<NotUsed, List<UserScore>> getTopScores();

    ServiceCall<List<ScoreRecord>, Done> registerScore();

    @Override
    default Descriptor descriptor() {
        // @formatter:off
        return named("score").withCalls(
                pathCall("/api/score/scores", this::getTopScores),
                pathCall("/api/score", this::registerScore)
        ).withAutoAcl(true);
        // @formatter:on
    }
}
