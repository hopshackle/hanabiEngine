package com.fossgalaxy.games.fireworks.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StateUtils {

    public static int getDiscardedCount(GameState state, CardColour colour, int value) {
        Collection<Card> discard = state.getDiscards();

        int total = 0;
        for (Card c : discard) {
            if (c.value == value) {
                total++;
            }
        }

        return total;
    }

    public static Collection<Integer> getMatching(GameState state, int player, CardColour c) {
        List<Integer> slots = new ArrayList<Integer>();

        Hand hand = state.getHand(player);
        for (int i = 0; i < hand.getSize(); i++) {
            // TODO do i use real or suspected values?
        }

        return slots;
    }

    public static Collection<Integer> getMatching(GameState state, int player, int value) {
        List<Integer> slots = new ArrayList<Integer>();

        Hand hand = state.getHand(player);
        for (int i = 0; i < hand.getSize(); i++) {
            // TODO do i use real or suspected values?
        }

        return slots;
    }

}
