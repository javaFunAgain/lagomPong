package pl.setblack.pongi.users;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import javaslang.control.Option;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

/**
 * Created by jarek on 1/14/17.
 */
public interface UsersService extends Service{
    ServiceCall<NewUser, RegUserStatus> addUser(String id);

    ServiceCall<LoginData, Option<Session>> login(String id);

    ServiceCall<NotUsed, Option<Session>> session(String  sessionId);

    @Override
    default Descriptor descriptor() {
        // @formatter:off
        return named("users").withCalls(
                pathCall("/api/users/add/:id",  this::addUser),
                pathCall("/api/users/login/:id",  this::login),
                pathCall("/api/users/session/:id",  this::session)
        ).withAutoAcl(true);
        // @formatter:on
    }
}
