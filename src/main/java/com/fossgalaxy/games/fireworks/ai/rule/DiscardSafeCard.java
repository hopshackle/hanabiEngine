package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.DiscardCard;

import java.util.Arrays;

/**
 * Created by webpigeon on 18/10/16.
 */
public class DiscardSafeCard extends AbstractDiscardRule {
    @Override
    public Action execute(int playerID, GameState state) {

        int[] tableCurr = new int[CardColour.values().length];
        int min = 5;

        for (CardColour c : CardColour.values()) {
            tableCurr[c.ordinal()] = state.getTableValue(c);
            if (tableCurr[c.ordinal()] < min) {
                min = tableCurr[c.ordinal()];
            }
        }

        Hand myHand = state.getHand(playerID);
        for (int slot = 0; slot < state.getHandSize(); slot++) {
            CardColour c = myHand.getKnownColour(slot);
            Integer value = myHand.getKnownValue(slot);

            if (c != null) {
                //even if we don't know the value, sometimes we know it's safe to discard
                if (value == null) {
                    if (tableCurr[c.ordinal()] == 5) {
                        return new DiscardCard(slot);
                    }
                } else if (tableCurr[c.ordinal()] > value) {
                    return new DiscardCard(slot);
                }
            } else if (value != null && value <= min) {
                return new DiscardCard(slot);
            }
        }

        return null;
    }
}
