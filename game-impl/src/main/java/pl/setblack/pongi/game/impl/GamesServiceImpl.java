package pl.setblack.pongi.game.impl;

import akka.NotUsed;
import akka.japi.Pair;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.MessageProtocol;
import com.lightbend.lagom.javadsl.api.transport.ResponseHeader;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import javaslang.collection.List;
import javaslang.control.Option;
import org.pcollections.HashTreePMap;
import pl.setblack.pongi.game.impl.info.GamesInfoCommand;
import pl.setblack.pongi.game.impl.info.GamesInfoEntity;
import pl.setblack.pongi.game.impl.state.GameStateCommand;
import pl.setblack.pongi.game.impl.state.GameStateEntity;
import pl.setblack.pongi.users.Session;
import pl.setblack.pongi.users.UsersService;

import javax.inject.Inject;
import java.time.Clock;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Supplier;


public class GamesServiceImpl implements GamesService {

    private final PersistentEntityRegistry persistentEntityRegistry;

    private  final UsersService usersService;

    private final Clock clock = Clock.systemUTC();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final AtomicLong sanityCounter = new AtomicLong();

    @Inject
    public GamesServiceImpl(UsersService usersService, PersistentEntityRegistry persistentEntityRegistry) {
        this.usersService = usersService;
        this.persistentEntityRegistry = persistentEntityRegistry;
        this.persistentEntityRegistry.register(GamesInfoEntity.class);
        this.persistentEntityRegistry.register(GameStateEntity.class);

        this.scheduler.scheduleAtFixedRate(()->pushGames(), 1000, 100,TimeUnit.MILLISECONDS);
    }

    private void pushGames() {
        long sanity = this.sanityCounter.decrementAndGet();
        if ( sanity > 0) {
            PersistentEntityRef<GamesInfoCommand> gameListRef = persistentEntityRegistry.refFor(GamesInfoEntity.class, "global");
            gameListRef.ask(new GamesInfoCommand.GetList())
                    .thenAccept((List<GameInfo> gameList) -> {
                        gameList.forEach((GameInfo game) -> {
                            PersistentEntityRef<GameStateCommand> gameStateRef = persistentEntityRegistry.refFor(GameStateEntity.class, game.uuid);
                            gameStateRef.ask(new GameStateCommand.PushGameLoop(clock.millis()));
                        });

                    });
        }
    }

    @Override
    public HeaderServiceCall<NotUsed, List<GameInfo>> games() {
        return runSecure((session,ignored) -> {
            PersistentEntityRef<GamesInfoCommand> ref = persistentEntityRegistry.refFor(GamesInfoEntity.class, "global");
            return ref.ask(new GamesInfoCommand.GetList());
        }, ()->List.empty() );
    }

    @Override
    public HeaderServiceCall<NotUsed, Option<GameState>> getGame(String uuid) {
        return runSecure((session,ignored) -> {
            this.sanityCounter.set(20);
            PersistentEntityRef<GameStateCommand> ref = persistentEntityRegistry.refFor(GameStateEntity.class, uuid);
            return ref.ask(new GameStateCommand.GetGame(uuid));
        }, ()->Option.none() );
    }

    @Override
    public HeaderServiceCall<String, Option<GameInfo>> create() {
        return runSecure((session, gameName) -> {

            PersistentEntityRef<GamesInfoCommand> ref = persistentEntityRegistry.refFor(GamesInfoEntity.class, "global");
            return ref.ask(new GamesInfoCommand.Create(gameName, session.userId))
                    .thenApply(Option::some);
        }, ()->Option.none() );
    }

    @Override
    public ServiceCall<String, Option<GameState>> join() {
       return runSecure((session, gameId) -> {
           final PersistentEntityRef<GamesInfoCommand> infoRef = persistentEntityRegistry.refFor(GamesInfoEntity.class, "global");
           return infoRef.ask(new GamesInfoCommand.JoinGame(gameId, session.userId))
                   .thenCompose((Option<GameInfo> gameInfoOption)->
                       gameInfoOption.map( gameInfo ->{
                           final PersistentEntityRef<GameStateCommand> gameStateRef = persistentEntityRegistry.refFor(GameStateEntity.class, gameId);
                           return gameStateRef.ask(new GameStateCommand.StartGame(gameInfo, clock.millis()));
                       }).getOrElse(CompletableFuture.completedFuture(Option.none()))
                   );

        }, ()->Option.<GameState>none() );
    }

    private <T,REQ> HeaderServiceCall<REQ, T > runSecure(BiFunction<Session, REQ, CompletionStage<T>> privilegedAction,
                                                         Supplier<T> insecureResult) {
        return (reqHeaders, requestData) -> {

            final Option<String> bearer = Option.ofOptional(reqHeaders.getHeader("Authorization"));
            System.out.println(bearer);
            final CompletionStage<Option<Session>> sessionCall = bearer.map(bs -> {
                final String sessionId = bs.replace("Bearer ","");
                return this.usersService.session(sessionId).invoke();
            }).getOrElse(CompletableFuture.completedFuture(Option.none()));
            return sessionCall.thenCompose(
                    session-> {
                        System.out.println("session is:" + session);
                        return session.map(ses-> privilegedAction.apply(ses, requestData)).getOrElse(CompletableFuture.completedFuture(insecureResult.get()))
                                .thenApply( stat-> Pair.create(getResponseStatus(session), stat));
                    }
            );

        };
    }

    private ResponseHeader getResponseStatus(final Option<Session> session) {
        return session.map( any -> ResponseHeader.OK).getOrElse( new ResponseHeader(401, new MessageProtocol(), HashTreePMap.empty()));
    }
}
