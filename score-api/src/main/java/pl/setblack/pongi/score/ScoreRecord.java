package pl.setblack.pongi.score;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.concurrent.Immutable;

/**
 * Created by jarek on 2/5/17.
 */
@Immutable
@JsonDeserialize
public class ScoreRecord {
    public final String userId;
    public final GameResult result;
    public final int playerScored;
    public final int opponentScore;
    public final String gameId;

    @JsonCreator
    public ScoreRecord(
            String userId,
            GameResult result,
            int playerScore,
            int opponentScore,
            String gameId) {
        this.userId = userId;
        this.result = result;
        this.playerScored = playerScore;
        this.opponentScore = opponentScore;
        this.gameId = gameId;
    }
}
