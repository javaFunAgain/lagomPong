package pl.setblack.pongi.game.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.concurrent.Immutable;

/**
 * Created by jarek on 1/23/17.
 */
@Immutable
@JsonDeserialize
public class Paddle extends GameObject {
    final float targetY;
    @JsonCreator
    public Paddle(float x, float y, float targetY) {
        super(x, y);
        this.targetY = targetY;
    }

    public Paddle(float x, float y) {
        this(x,y,y);
    }

    public Paddle paddleMove(long timeDiff) {
        float distanceToTarget = targetY - y;
        float direction = Math.signum(distanceToTarget);
        float maxMove = timeDiff/ 5000.0f;
        float realMove = Math.min(Math.abs(distanceToTarget),maxMove);
        float newY = this.y + (direction)*realMove;
        return new Paddle(this.x, newY, targetY);
    }

    public Paddle movingTo(float targetY) {
        return new Paddle(this.x,this.y,targetY);
    }
}
