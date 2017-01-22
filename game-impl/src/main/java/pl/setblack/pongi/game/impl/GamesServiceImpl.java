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
import pl.setblack.pongi.users.Session;
import pl.setblack.pongi.users.UsersService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
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
        return runSecure((session,ignored) -> {
            PersistentEntityRef<GamesInfoCommand> ref = persistentEntityRegistry.refFor(GamesInfoEntity.class, "global");
            return ref.ask(new GamesInfoCommand.GetList());
        }, ()->List.empty() );
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
    public ServiceCall<String, GameState> join() {
        return null;
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
