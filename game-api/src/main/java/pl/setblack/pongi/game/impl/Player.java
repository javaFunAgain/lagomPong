package pl.setblack.pongi.game.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.concurrent.Immutable;

@Immutable
@JsonDeserialize
public class Player {
    public final int score;
    public final String name;
    public final Paddle paddle;

    @JsonCreator
    public Player(int score, String name, Paddle paddle) {
        this.score = score;
        this.name = name;
        this.paddle = paddle;
    }

    public Player movePaddle(long dist) {
        return new Player(this.score, this.name, this.paddle.paddleMove(dist));
    }

    public Player makeMoving(String userId, float targetY) {
        return (this.name.equals(userId))
                ? new Player(this.score, this.name, this.paddle.movingTo(targetY))
                : this;

    }
}
