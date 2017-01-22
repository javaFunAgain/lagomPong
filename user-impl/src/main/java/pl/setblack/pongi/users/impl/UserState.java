package pl.setblack.pongi.users.impl;

import javaslang.control.Option;

import javax.annotation.concurrent.Immutable;

/**
 * Created by jarek on 1/14/17.
 */
@Immutable
public class UserState {
    public final Option<UserData> user  ;


    public UserState(final Option<UserData> user) {
        this.user = user;
    }


    public static UserState empty() {
        return new UserState(Option.none());
    }
}
