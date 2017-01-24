package pl.setblack.pongi.game.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.concurrent.Immutable;

@Immutable
@JsonDeserialize
public class Player {
    final int score;
    final String name;
    final Paddle paddle;

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
        if ( this.name.equals(userId)) {
            return new Player(this.score, this.name, this.paddle.movingTo(targetY));
        } else{
            return this;
        }
    }
}
