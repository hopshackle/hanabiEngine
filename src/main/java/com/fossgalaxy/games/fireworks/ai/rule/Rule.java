package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;

@FunctionalInterface
public interface Rule {

    default boolean canFire(int playerID, GameState state) {
        Action returnedAction = execute(playerID, state);
        return returnedAction != null;
    }

    Action execute(int playerID, GameState state);

}
