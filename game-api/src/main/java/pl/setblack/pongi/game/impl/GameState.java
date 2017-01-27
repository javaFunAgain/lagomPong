package pl.setblack.pongi.game.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javaslang.Tuple2;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.function.Function;

@Immutable
@JsonDeserialize
public class GameState implements Serializable {

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
          return new GameState(
                Ball.random( ),
                this.players,
                startTime);

    }

    public GameState push(long newTime) {
        long diff = newTime - this.updateTime;
        float scale = diff / 5.0f;
        final Ball newBallPos = this.ball.bounce(this.players.map( pl->pl.paddle, pl->pl.paddle)).move(scale);
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
