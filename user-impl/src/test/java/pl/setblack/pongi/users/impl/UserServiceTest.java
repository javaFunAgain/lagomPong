package pl.setblack.pongi.users.impl;

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
    public void testSingleCreateUser() {
        withServer(defaultSetup().withCassandra(true), server -> {
            UsersService service = server.client(UsersService.class);
            RegUserStatus created = service.addUser("aaa").invoke(new NewUser("aaa")).toCompletableFuture().get(5, SECONDS);
            assertEquals(true, created.ok);

        });
    }

    @Test
    public void testDoubleCreateUser() {
        withServer(defaultSetup().withCassandra(true), server -> {
            UsersService service = server.client(UsersService.class);
            RegUserStatus created1st = service.addUser("aaa").invoke(new NewUser("aaa")).toCompletableFuture().get(5, SECONDS);
            RegUserStatus created2nd= service.addUser("aaa").invoke(new NewUser("aaa")).toCompletableFuture().get(5, SECONDS);

            assertEquals(true, created2nd.ok);

        });
    }
}
