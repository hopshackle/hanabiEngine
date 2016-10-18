package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.DebugUtils;
import com.fossgalaxy.games.fireworks.ai.rule.logic.DeckUtils;
import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.PlayCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by webpigeon on 12/10/16.
 */
public class PlaySafeCard extends AbstractRule {

    @Override
    public Action execute(int playerID, GameState state) {
        //descretise my hand
        Map<Integer, List<Card>> possibleCards = DeckUtils.bindBlindCard(playerID, state.getHand(playerID), state.getDeck().toList());

        //figure out what cards are playable
        List<Integer> playableSlots = possibleCards.entrySet().stream()
                .filter(x -> x.getValue().stream().allMatch(y -> isPlayable(y, state)))
               .map(Map.Entry::getKey)
               .collect(Collectors.toList());

        //if nothing is garenteed to be playable, this rule doesn't fire
        if (playableSlots.isEmpty()) {
            return null;
        }

        return new PlayCard(playableSlots.get(0));
    }


    public static void main(String[] args) {
        GameState state = new BasicState(5, 2);
        state.init(0L);

        state.getHand(0).setKnownValue(1, new Integer[]{3,4});
        DebugUtils.printState(System.out, state);

        PlaySafeCard psc = new PlaySafeCard();

        System.out.println(psc.execute(0, state));
    }

    public boolean isPlayable(Card card, GameState state) {
       return state.getTableValue(card.colour) + 1 == card.value;
    }

}
