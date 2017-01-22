package pl.setblack.pongi.game.impl.info;


import com.lightbend.lagom.serialization.Jsonable;
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
        throw new UnsupportedOperationException();
    }

}
