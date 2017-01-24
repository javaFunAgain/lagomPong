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

    public Ball move(float scale) {
        return new Ball( this.x + speed.x*scale, this.y + speed.y*scale, this.speed);
    }

    public Ball bounceX() {
        if ( this.x < 0 && speed.x < 0) {
            return new Ball(0f, this.y, this.speed.bounceX());
        }
        if ( this.x > 1.0f && speed.x > 0) {
            return new Ball(1f, this.y, this.speed.bounceX());
        }
        return this;
    }
    public Ball bounceY() {
        if ( this.y < 0 && speed.y < 0) {
            return new Ball(this.x, 0f, this.speed.bounceY());
        }
        if ( this.y > 1.0f && speed.x > 0) {
            return new Ball(this.x, 1.0f, this.speed.bounceY());
        }
        return this;
    }


    public Ball bounce() {
        return this.bounceY().bounceX();
    }
}

