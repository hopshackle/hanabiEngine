package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.DebugUtils;
import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.iggi.IGGIFactory;
import com.fossgalaxy.games.fireworks.state.*;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;

public class TellAnyoneAboutUsefulCard extends AbstractTellRule {

    @Override
    public Action execute(int playerID, GameState state) {
        if (state.getInfomation() == 0) {
            return null;
        }

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


    public static void main(String[] args) {
        GameState state = new BasicState(5, 2);
        state.init(2l);

        DebugUtils.printState(System.out, state);

        Agent pra = IGGIFactory.buildCautious();

        Action action = pra.doMove(0, state);
        action.apply(0, state);
        System.out.println(action);


        Action action3 = pra.doMove(0, state);
        action3.apply(0, state);
        System.out.println(action3);

        DebugUtils.printState(System.out, state);

        Action action2 = pra.doMove(1, state);
        action2.apply(1, state);
        System.out.println(action2);

        DebugUtils.printState(System.out, state);

    }

}
