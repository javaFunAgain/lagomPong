package pl.setblack.pongi.game.impl;

import akka.NotUsed;
import akka.japi.Pair;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.ResponseHeader;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import javaslang.collection.List;
import javaslang.control.Option;
import pl.setblack.pongi.game.impl.info.GamesInfoCommand;
import pl.setblack.pongi.game.impl.info.GamesInfoEntity;
import pl.setblack.pongi.users.Session;
import pl.setblack.pongi.users.UsersService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.function.Supplier;


public class GamesServiceImpl implements GamesService {

    private final PersistentEntityRegistry persistentEntityRegistry;

    private  final UsersService usersService;

    @Inject
    public GamesServiceImpl(UsersService usersService, PersistentEntityRegistry persistentEntityRegistry) {
        this.usersService = usersService;
        this.persistentEntityRegistry = persistentEntityRegistry;
        this.persistentEntityRegistry.register(GamesInfoEntity.class);
    }

    @Override
    public HeaderServiceCall<NotUsed, List<GameInfo>> games() {
        return runSecure(session -> {
            PersistentEntityRef<GamesInfoCommand> ref = persistentEntityRegistry.refFor(GamesInfoEntity.class, "global");
            return ref.ask(new GamesInfoCommand.GetList());
        }, ()->List.empty() );
    }

    private <T> HeaderServiceCall<NotUsed, T > runSecure(Function<Session, CompletionStage<T>> privilegedAction,
                                                                     Supplier<T> insecureResult) {
        return (reqHeaders, any) -> {

            final Option<String> bearer = Option.ofOptional(reqHeaders.getHeader("Authorization"));
            System.out.println(bearer);
            final CompletionStage<Option<Session>> sessionCall = bearer.map(bs -> {
                final String sessionId = bs.replace("Bearer ","");
                return this.usersService.session(sessionId).invoke();
            }).getOrElse(CompletableFuture.completedFuture(Option.none()));
            return sessionCall.thenCompose(
                    session-> {
                        System.out.println("session is:" + session);
                        return session.map( privilegedAction).getOrElse(CompletableFuture.completedFuture(insecureResult.get()))
                        .thenApply( stat-> Pair.create(ResponseHeader.OK, stat));
                    }
            );

        };
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
