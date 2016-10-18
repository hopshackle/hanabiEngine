package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;

/**
 * This rule is damage control - we can't get a perfect score because we have discarded a prerequisite card, now we
 * know it's safe to discard higher value cards of the same suit.
 */
public class DiscardUselessCard extends AbstractRule {

    //TODO if the highest in the suit has already been reached, then discard this card.
    @Override
    public Action execute(int playerID, GameState state) {
        return null;
    }

}
