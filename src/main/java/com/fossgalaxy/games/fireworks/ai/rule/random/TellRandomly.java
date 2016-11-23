package com.fossgalaxy.games.fireworks.ai.rule.random;

import com.fossgalaxy.games.fireworks.ai.rule.AbstractRule;
import com.fossgalaxy.games.fireworks.ai.rule.AbstractTellRule;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;

import java.util.Random;

/**
 * Tell the next player about a card randomly.
 */
public class TellRandomly extends AbstractTellRule {
    private Random random;

    public TellRandomly() {
        this.random = new Random();
    }

    @Override
    public Action execute(int playerID, GameState state) {

        int nextAgent = selectPlayer(playerID, state);
        Hand otherHand = state.getHand(nextAgent);

        //Select a random card from the player's hand
        int slot = random.nextInt(state.getHandSize());
        Card card = otherHand.getCard(slot);

        //if this card doens't exist, then abort this move (handle this better)
        if (card == null) {
            return null;
        }

        //decide if we should describe the colour or number
        if (random.nextBoolean()) {
            return new TellValue(nextAgent, card.value);
        } else {
            return new TellColour(nextAgent, card.colour);
        }
    }

}
