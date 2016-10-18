package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;

/**
 * Created by webpigeon on 18/10/16.
 */
public abstract class AbstractTellRule extends AbstractRule {

    @Override
    public boolean canFire(int playerID, GameState state) {
        if (state.getInfomation() == 0) {
            return false;
        }

        Action action = execute(playerID, state);
        return action != null;
    }

}
