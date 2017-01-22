package pl.setblack.pongi.users.impl;

import akka.NotUsed;
import akka.japi.Pair;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.ResponseHeader;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import javaslang.control.Option;
import pl.setblack.pongi.sessions.impl.SessionsCommand;
import pl.setblack.pongi.sessions.impl.SessionsEntity;
import pl.setblack.pongi.users.*;
import scala.Unit;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class UsersServiceImpl implements UsersService {

    private final PersistentEntityRegistry persistentEntityRegistry;

    @Inject
    public UsersServiceImpl(PersistentEntityRegistry persistentEntityRegistry) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        this.persistentEntityRegistry.register(UserEntity.class);
        this.persistentEntityRegistry.register(SessionsEntity.class);
    }

    @Override
    public HeaderServiceCall<NewUser, RegUserStatus> addUser(String id) {
        return (reqHeaders, postedUser) -> {
            System.out.println(reqHeaders.getHeader("Referer"));
            PersistentEntityRef<UserCommand> ref = persistentEntityRegistry.refFor(UserEntity.class, id);
            return ref.ask(new UserCommand.RegisterUser(id, postedUser)).thenApply( stat -> Pair.create(ResponseHeader.OK, stat));
        };
    }

    @Override
    public ServiceCall<LoginData, Option<Session>> login(String id) {
        return (LoginData request) -> {
            PersistentEntityRef<UserCommand> ref = persistentEntityRegistry.refFor(UserEntity.class, id);
            return ref.ask(new UserCommand.LoginUser(id, request)).thenCompose( (Boolean logged )-> createSession(id, logged));
        };
    }

    private CompletionStage<Option<Session>> createSession(final String userId , boolean logged) {
        if (logged) {
              PersistentEntityRef< SessionsCommand> ref = persistentEntityRegistry
                    .refFor(SessionsEntity.class, "global");
                    return ref.ask( new SessionsCommand.StartSession(userId));

        } else {
            return CompletableFuture.completedFuture(Option.none());
        }
    }

    @Override
    public ServiceCall<NotUsed, Option<Session>> session(String sessionId) {
        return  request -> {
            PersistentEntityRef<SessionsCommand> ref = persistentEntityRegistry.refFor(SessionsEntity.class, "global");
            return ref.ask(new SessionsCommand.GetSession(sessionId));
        };
    }
}
