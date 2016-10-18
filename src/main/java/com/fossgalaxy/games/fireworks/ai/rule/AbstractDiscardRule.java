package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;

/**
 * Created by webpigeon on 18/10/16.
 */
public abstract class AbstractDiscardRule implements Rule {

    @Override
    public boolean canFire(int playerID, GameState state) {
        //discard rules can never fire if there is full information
        if (state.getInfomation() == state.getStartingInfomation()) {
            return false;
        }

        Action action = execute(playerID, state);
        return action != null;
    }

}
