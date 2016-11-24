package com.fossgalaxy.games.fireworks.ai.rule.random;

import com.fossgalaxy.games.fireworks.ai.rule.AbstractDiscardRule;
import com.fossgalaxy.games.fireworks.ai.rule.AbstractRule;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.DiscardCard;

import java.util.Random;

public class DiscardRandomly extends AbstractDiscardRule {
    private Random random;

    public DiscardRandomly() {
        this.random = new Random();
    }

    @Override
    public Action execute(int playerID, GameState state) {
        int randomDiscard = random.nextInt(state.getHandSize());
        return new DiscardCard(randomDiscard);
    }

}
