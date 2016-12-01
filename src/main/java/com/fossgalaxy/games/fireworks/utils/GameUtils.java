package com.fossgalaxy.games.fireworks.utils;

import com.fossgalaxy.games.fireworks.GameRunner;
import com.fossgalaxy.games.fireworks.GameStats;
import com.fossgalaxy.games.fireworks.players.Player;

/**
 * This contains tools to help run games in a consistent, reproducible way.
 */
public final class GameUtils {

    private GameUtils() {

    }

    public static GameStats runGame(String gameID, Long seed, Player... players) {
        assert gameID != null;
        assert players != null;

        GameRunner runner = new GameRunner(gameID, players.length);

        for (Player player : players) {
            runner.addPlayer(player);
        }

        return runner.playGame(seed);
    }

}
