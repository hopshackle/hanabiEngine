package com.fossgalaxy.games.fireworks.state.events;

import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;

import java.util.*;

/**
 * Created by webpigeon on 28/11/16.
 */
public class CheatEvent extends GameEvent {
    private int playerID;
    private Hand hand;

    public CheatEvent(int playerID, Hand hand) {
        super(MessageType.CHEAT);
        this.playerID = playerID;
        this.hand = hand;
    }

    @Override
    public void apply(GameState state) {
        Hand hand = state.getHand(playerID);
        Hand serverHand = this.hand;

        Map<Integer, List<Integer>> values = new HashMap<>();
        Map<CardColour, List<Integer>> colours = new EnumMap<>(CardColour.class);

        for (int i=0; i<hand.getSize(); i++) {
            Card card = serverHand.getCard(i);
            values.computeIfAbsent(card.value, ArrayList::new).add(i);
            colours.computeIfAbsent(card.colour, (CardColour c) -> new ArrayList()).add(i);
        }

        for (Map.Entry<Integer, List<Integer>> entry : values.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }
            hand.setKnownValue(entry.getKey(), entry.getValue().toArray(new Integer[0]));
        }

        for (Map.Entry<CardColour, List<Integer>> entry : colours.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }
            hand.setKnownColour(entry.getKey(), entry.getValue().toArray(new Integer[0]));
        }
    }

    @Override
    public boolean isVisibleTo(int playerID) {
        //return true;
        return playerID == this.playerID;
    }

    @Override
    public String toTextProtocol() {
        return null;
    }
}
