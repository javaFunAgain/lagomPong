package pl.setblack.pongi.game.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import javaslang.collection.List;
import javaslang.control.Option;
import scala.Unit;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

/**
 * Created by jarek on 1/14/17.
 */
public interface GamesService extends Service{
    ServiceCall<NotUsed, List<GameInfo>> games();

    ServiceCall<String, Option<GameInfo>> create();

    ServiceCall<String, Option<GameState>> join();

    @Override
    default Descriptor descriptor() {
        // @formatter:off
        return named("games").withCalls(
                pathCall("/api/games/games",  this::games),
                pathCall("/api/games/create",  this::create),
                pathCall("/api/games/join",  this::join)
        ).withAutoAcl(true);
        // @formatter:on
    }
}
