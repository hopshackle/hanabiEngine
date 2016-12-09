package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;

/**
 * Created by piers on 09/12/16.
 *
 * This will tell the most information to a hand
 * but doesn't at the moment cover what they already know
 *
 * From Van Den Bergh paper
 */
public class TellMostInformation extends AbstractTellRule {

    @Override
    public Action execute(int playerID, GameState state) {
        Action bestAction = null;
        int bestAffected = 0;
        for (int player = 0; player < state.getPlayerCount(); player++) {
            if (player == playerID) {
                continue;
            }
            // Get all possible hints for this player
            Hand hand = state.getHand(player);

            for (int i = 1; i <= 5; i++) {
                int totalAffected = 0;
                for (int slot = 0; slot < state.getHandSize(); slot++) {
                    if (hand.hasCard(slot)) {
                        if (hand.getCard(slot).value == i) {
                            totalAffected++;
                        }
                    }
                }
                if (totalAffected > bestAffected) {
                    bestAction = new TellValue(player, i);
                    bestAffected = totalAffected;
                }
            }

            for (CardColour colour : CardColour.values()) {
                int totalAffected = 0;
                for (int slot = 0; slot < state.getHandSize(); slot++) {
                    if (hand.hasCard(slot)) {
                        if (hand.getCard(slot).colour == colour) {
                            totalAffected++;
                        }
                    }
                }
                if(totalAffected > bestAffected){
                    bestAction = new TellColour(player, colour);
                    bestAffected = totalAffected;
                }
            }
        }

        return bestAction;
    }

    @Override
    public boolean canFire(int playerID, GameState state) {
        return state.getInfomation() > 0;
    }
}
