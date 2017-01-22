package pl.setblack.pongi.users.impl;

import com.lightbend.lagom.javadsl.api.transport.RequestHeader;
import org.junit.Test;
import pl.setblack.pongi.users.NewUser;
import pl.setblack.pongi.users.RegUserStatus;
import pl.setblack.pongi.users.UsersService;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.withServer;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

/**
 * Created by jarek on 1/19/17.
 */
public class UserServiceTest {
    @Test
    public void testIt() {
        withServer(defaultSetup(), server -> {
            UsersService service = server.client(UsersService.class);
            RegUserStatus created = service.addUser("aaa").handleRequestHeader(
                    rh -> rh.withHeader("Referer" ,"winter")
            ).invoke(new NewUser("aaa")).toCompletableFuture().get(5, SECONDS);
            assertEquals(true, created.ok); // default greeting

        });
    }
}
