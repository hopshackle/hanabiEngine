package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.ai.rule.logic.DeckUtils;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.DiscardCard;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This rule is damage control - we can't get a perfect score because we have discarded a prerequisite card, now we
 * know it's safe to discard higher value cards of the same suit.
 */
public class DiscardUselessCard extends AbstractDiscardRule {
    private final int[] numCards = {0, 3, 2, 2, 2, 1}; //zeroth value should never occur

    @Override
    public Action execute(int playerID, GameState state) {

        Hand myHand = state.getHand(playerID);
        Map<Integer, List<Card>> possibleCards = null;
        for (int slot = 0; slot < myHand.getSize(); slot++) {
            CardColour c = myHand.getKnownColour(slot);
            if (c == null) {
                continue;
            }

            int highestPossible = getHighestScore(state, c);
            Integer knownValue = myHand.getKnownValue(slot);
            if (knownValue != null && highestPossible < knownValue) {
                return new DiscardCard(slot);
            }

            if (possibleCards == null) {
                possibleCards = DeckUtils.bindBlindCard(playerID, myHand, state.getDeck().toList());
            }

            if (possibleCards.containsKey(slot)) {
                int minimum = possibleCards.get(slot).stream().mapToInt(x -> x.value).min().getAsInt();
                if (minimum > highestPossible) {
                    return new DiscardCard(slot);
                }
            }
        }

        return null;
    }

    /**
     * Calculates the maximum possible score obtainable for the given colour
     *
     * @param state  The game state
     * @param colour The colour to look at
     * @return The maximum possible score
     */
    public int getHighestScore(GameState state, CardColour colour) {
        int nextValue = state.getTableValue(colour) + 1;

        Collection<Card> discards = state.getDiscards();
        for (int i = nextValue; i <= 5; i++) {
            int occurrences = Collections.frequency(discards, new Card(i, colour));

            //if all of this value have already been discarded, the previous value is the highest
            if (occurrences == numCards[i]) {
                return i - 1;
            }

        }

        //if we didn't find any doomed combinations, we're probably still good
        return 5;
    }

}
