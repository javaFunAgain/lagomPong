package pl.setblack.pongi.game.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.concurrent.Immutable;


@Immutable
@JsonDeserialize
public class GameInfo {
    public final String name;

    public final String uuid;

    @JsonCreator
    public GameInfo(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }
}
