package pl.setblack.pongi.users.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import pl.setblack.pongi.users.LoginData;
import pl.setblack.pongi.users.NewUser;
import pl.setblack.pongi.users.RegUserStatus;

import javax.annotation.concurrent.Immutable;

/**
 * Created by jarek on 1/14/17.
 */
public interface UserCommand extends Jsonable {

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    class RegisterUser implements UserCommand , CompressedJsonable, PersistentEntity.ReplyType<RegUserStatus>{
        public final String userId;
        public final NewUser data;

        @JsonCreator
        public RegisterUser(String userId, NewUser data) {
            this.userId = userId;
            this.data = data;
        }
    }

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    class LoginUser implements UserCommand , CompressedJsonable, PersistentEntity.ReplyType<Boolean>{
        public final String userId;
        public final LoginData data;

        @JsonCreator
        public LoginUser(String userId, LoginData data) {
            this.userId = userId;
            this.data = data;
        }
    }


    }
