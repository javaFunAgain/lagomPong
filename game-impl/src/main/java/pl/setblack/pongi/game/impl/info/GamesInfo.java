package pl.setblack.pongi.game.impl.info;


import javaslang.collection.List;
import javaslang.control.Option;
import pl.setblack.pongi.game.impl.GameInfo;

import javax.annotation.concurrent.Immutable;

@Immutable
public class GamesInfo{
    public final List<GameInfo> allGames;

    public GamesInfo(List<GameInfo> allGames) {
        this.allGames = allGames;
    }

    public GamesInfo() {
        this(List.empty());
    }

    public GamesInfo withGame(final GameInfo info) {
        return new GamesInfo(allGames.append(info));
    }


    public Option<GameInfo> myGame(final String userId) {
        return this.allGames.filter( gi -> gi.players.contains(userId)).headOption();
    }

    public GamesInfo joinGame(final String gameId, final String userId) {
        final Option<GameInfo> game = findGame(gameId);
        final Option<GameInfo> newOne =
                game.flatMap ( oldGame -> oldGame.withPlayer(userId));
        return newOne.map( newGame ->
            new GamesInfo(this.allGames
                    .removeAll(g->g.uuid.equals(gameId))
                    .append(newGame))
        ).getOrElse(this);
    }

    public Option<GameInfo> findGame(String gameId) {
        return this.allGames.filter( gi -> gi.uuid.equals(gameId)).headOption();
    }
}
