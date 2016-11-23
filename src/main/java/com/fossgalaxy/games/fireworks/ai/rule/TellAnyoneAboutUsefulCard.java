package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.DebugUtils;
import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.iggi.IGGIFactory;
import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;

/**
 * Tell any other player about a card in their hand if it is useful in this situation.
 */
public class TellAnyoneAboutUsefulCard extends AbstractTellRule {

    @Override
    public Action execute(int playerID, GameState state) {

        for (int i = 0; i < state.getPlayerCount(); i++) {
            int nextPlayer = (playerID + i) % state.getPlayerCount();
            Hand hand = state.getHand(nextPlayer);

            //gard against trying to tell ourselves things
            if (nextPlayer == playerID) {
                continue;
            }

            for (int slot = 0; slot < state.getHandSize(); slot++) {

                Card card = hand.getCard(slot);
                if (card == null) {
                    continue;
                }

                int currTable = state.getTableValue(card.colour);
                if (card.value != currTable + 1) {
                    continue;
                }

                if (hand.getKnownValue(slot) == null) {
                    return new TellValue(nextPlayer, card.value);
                } else if (hand.getKnownColour(slot) == null) {
                    return new TellColour(nextPlayer, card.colour);
                }
            }
        }
        return null;
    }
}
