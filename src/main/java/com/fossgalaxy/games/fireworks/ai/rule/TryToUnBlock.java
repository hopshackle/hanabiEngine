package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.ai.rule.logic.HandUtils;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.Action;

/**
 * Created by piers on 04/05/17.
 *
 * How complex? Is Player 2 able to play if it relies on Player 1 playing a card that they know about?
 */
public class TryToUnBlock implements Rule {

    @Override
    public Action execute(int playerID, GameState state) {

        // Need to track this
        int information = state.getInfomation();
        for (int i = 0; i < state.getPlayerCount(); i++) {
            int lookingAt = (playerID + i) % state.getPlayerCount();

            if (isUnblocking(state, lookingAt)) return null;
            Hand hand = state.getHand(playerID);

            // We have already checked discard and playable 5
            for (int slot = 0; slot < hand.getSize(); slot++) {
                //
            }

            // Is this player blocking
        }

        return null;
    }

    public boolean isUnblocking(GameState state, int playerID) {
        Hand hand = state.getHand(playerID);

        for (int slot = 0; slot < hand.getSize(); slot++) {
            if (HandUtils.isSafeToDiscard(state, hand.getKnownColour(slot), hand.getKnownValue(slot))) {
                return true;
            }

            if (hand.getKnownValue(slot) == 5) {
                if (hand.getKnownColour(slot) != null) {
                    if (state.getTableValue(hand.getKnownColour(slot)) == 4) {
                        return true;
                    }
                }
                // TODO Need to expand this a little
            }
        }

        return false;
    }

    public boolean isBlocked(GameState state, int playerID) {
        Hand hand = state.getHand(playerID);

        for (int slot = 0; slot < hand.getSize(); slot++) {
            // Return false if this card is either playable or discardable
        }

        return true;
    }
}
