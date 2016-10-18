package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.TimedHand;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.DiscardCard;

/**
 * Created by webpigeon on 18/10/16.
 */
public class DiscardOldestFirst extends AbstractRule {

    @Override
    public Action execute(int playerID, GameState state) {

        //we're not allowed to discard on full infomation
        if (state.getInfomation() == state.getStartingInfomation()) {
            return null;
        }

        //we can't fire if you didn't use a timed hand
        Hand hand = state.getHand(playerID);
        if (! (hand instanceof TimedHand) ) {
            return null;
        }

        TimedHand timedHand = (TimedHand)state.getHand(playerID);
        int slotID = timedHand.getOldestSlot();

        return new DiscardCard(slotID);
    }

}
