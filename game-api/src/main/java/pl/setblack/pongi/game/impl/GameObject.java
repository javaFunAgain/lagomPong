package pl.setblack.pongi.game.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.concurrent.Immutable;

/**
 * Created by jarek on 1/23/17.
 */
@Immutable
@JsonDeserialize
public class GameObject {
    public final float x;
    public final float y;

    @JsonCreator
    public GameObject(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
