package pl.setblack.pongi.game.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.concurrent.Immutable;

/**
 * Created by jarek on 1/23/17.
 */
@Immutable
@JsonDeserialize
public class Ball extends GameObject {
    public final Vector2D speed;

    @JsonCreator
    public Ball(float x, float y, Vector2D speed) {
        super(x, y);
        this.speed = speed;
    }

    public Ball(float x, float y) {
        this(x,y, new Vector2D(0f,0f));
    }

    public Ball withSpeed( Vector2D newSpeed) {
        return new Ball(this.x,this.y, newSpeed);
    }
}
