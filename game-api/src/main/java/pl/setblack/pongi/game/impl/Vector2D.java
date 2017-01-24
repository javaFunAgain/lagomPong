package pl.setblack.pongi.game.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.concurrent.Immutable;

/**
 * Created by jarek on 1/24/17.
 */
@Immutable
@JsonDeserialize
public class Vector2D {
    public final float x;
    public final float y;

    @JsonCreator
    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D() {
        this(0f,0f);
    }

    public static Vector2D fromAngle(double angle, double length) {
        float nx = (float)(Math.sin(angle)*length);
        float ny = (float)(Math.cos(angle)*length);
        return new Vector2D(nx,ny);
    }

}
