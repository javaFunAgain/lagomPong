package pl.setblack.pongi.game.impl;

import akka.Done;
import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import javaslang.collection.List;
import javaslang.control.Option;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

/**
 * Created by jarek on 1/14/17.
 */
public interface GamesService extends Service{
    ServiceCall<NotUsed, List<GameInfo>> games();

    ServiceCall<String, Option<GameInfo>> create();

    ServiceCall<String, Option<GameState>> join();

    ServiceCall<NotUsed, Option<GameState>> getGame(String uuid);

    ServiceCall<String, Done> movePaddle(final String gameId) ;

    ServiceCall<Source<String, NotUsed>, Source<GameState, NotUsed>> stream(final String gameId);


    @Override
    default Descriptor descriptor() {
        // @formatter:off
        return named("games").withCalls(
                pathCall("/api/games/games",  this::games),
                pathCall("/api/games/create",  this::create),
                pathCall("/api/games/join",  this::join),
                pathCall("/api/games/stream/:uuid", this::stream),
                pathCall("/api/games/:uuid",  this::getGame),
                pathCall("/api/games/move/:uuid",  this::movePaddle)

        ).withAutoAcl(true);
        // @formatter:on
    }
}
