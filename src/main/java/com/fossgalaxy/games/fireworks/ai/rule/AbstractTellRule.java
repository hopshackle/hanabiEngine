package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;

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

    public Action tellMissing(Hand hand, int playerID, int slot) {
        Card card = hand.getCard(playerID);
        if (hand.getKnownColour(slot) == null) {
            return new TellColour(playerID, card.colour);
        } else if (hand.getKnownValue(playerID) == null) {
            return new TellValue(playerID, card.value);
        }

        return null;
    }

    @Override
    public boolean couldFire(int playerID, GameState state) {
        return state.getInfomation() != 0;
    }
}
