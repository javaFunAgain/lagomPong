package pl.setblack.pongi.game.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javaslang.Tuple2;

import javax.annotation.concurrent.Immutable;
import java.util.function.Function;

@Immutable
@JsonDeserialize
public class GameState {

    public final Ball ball;
    public final Tuple2<Player, Player> players;
    public final long updateTime;


    @JsonCreator
    public GameState( Ball ball, Tuple2<Player, Player> players, long updateTime) {
        this.ball = ball;
        this.players = players;
        this.updateTime = updateTime;
    }

    public GameState start(long startTime) {
        final double randomAngle = Math.random()*Math.PI*2.0;
        final Vector2D speed = Vector2D.fromAngle(randomAngle, 0.01);
        return new GameState(
                this.ball.withSpeed(speed),
                this.players,
                startTime);

    }

    public GameState push(long newTime) {
        long diff = newTime - this.updateTime;
        float scale = diff / 1000.0f;
        final Ball newBallPos = this.ball.move(scale).bounce();
        final Function<Player,Player> movePaddle = player -> player.movePaddle(diff);

        final Tuple2<Player,Player> newPlayers = this.players.map(movePaddle,movePaddle);
        return new GameState(newBallPos, newPlayers,newTime);
    }


    public GameState playerMovingTo(String userId, float targetY) {
        final Function<Player,Player> movePaddle = player -> player.makeMoving(userId, targetY);
        final Tuple2<Player,Player> newPlayers = this.players.map(movePaddle,movePaddle);
        return new GameState(this.ball, newPlayers,this.updateTime);
    }
}
