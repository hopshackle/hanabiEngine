package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;

import java.util.Arrays;
import java.util.stream.Collectors;

@FunctionalInterface
public interface Rule {

    default boolean canFire(int playerID, GameState state) {
        Action returnedAction = execute(playerID, state);
        return returnedAction != null;
    }

    /**
     * Return true if it is possible this rule could fire, and return false if you can guarantee that it cannot fire.
     * @param playerID The player id that is this turn
     * @param state The state of the board
     * @return wether we could fire for that player in this state
     */
    default boolean couldFire(int playerID, GameState state){
        return true;
    }

    Action execute(int playerID, GameState state);

    default String fancyName(){
        return String.join(" ", (this.getClass().getSimpleName().split("(?=\\p{Upper})")));
    }

}
