package pl.setblack.pongi.game.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javaslang.collection.List;

import javax.annotation.concurrent.Immutable;


@Immutable
@JsonDeserialize
public class GameInfo {
    public final String name;

    public final String uuid;

    public final List<String> players;

    @JsonCreator
    public GameInfo(String name, String uuid, String player1) {
        this.name = name;
        this.uuid = uuid;
        this.players = List.of(player1);
    }
}
