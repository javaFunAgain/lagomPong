package pl.setblack.pongi.game.impl.info;


import javaslang.collection.List;
import pl.setblack.pongi.game.impl.GameInfo;

import javax.annotation.concurrent.Immutable;

@Immutable
public class GamesInfo {
    public final List<GameInfo> allGames;

    public GamesInfo(List<GameInfo> allGames) {
        this.allGames = allGames;
    }

    public GamesInfo() {
        this(List.empty());
    }


}
