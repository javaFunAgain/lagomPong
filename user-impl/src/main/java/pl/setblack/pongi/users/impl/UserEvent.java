package pl.setblack.pongi.users.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.Jsonable;
import pl.setblack.pongi.users.NewUser;

import javax.annotation.concurrent.Immutable;

/**
 * Created by jarek on 1/14/17.
 */
public interface UserEvent extends Jsonable {

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    final class RegisteredUser implements  UserEvent {
        public final String userId;
        public final NewUser data;

        @JsonCreator
        public RegisteredUser(String id, NewUser data) {
            this.userId = id;
            this.data = data;
        }
    }
}
