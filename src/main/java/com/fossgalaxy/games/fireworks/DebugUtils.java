package com.fossgalaxy.games.fireworks;

import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by webpigeon on 11/10/16.
 */
public class DebugUtils {

    public static void printState(PrintStream out, GameState state) {

        out.println();
        out.println("BEGIN STATE");

        out.format("%d players, information: %d, lives: %d, score: %d\n",
                state.getPlayerCount(),
                state.getInfomation(),
                state.getLives(),
                state.getScore());

        printTable(out, state);
        printHands(out, state);
        printDeck(out, state);
        printDiscard(out, state);

        out.println("END STATE");
    }

    public static void printTable(PrintStream out, GameState state){
        out.println("table value: ");
        Arrays.stream(CardColour.values()).forEach(c -> out.format("\t%10s %d\n", c, state.getTableValue(c)));
    }

    public static void printHands(PrintStream out, GameState state) {
        out.println("hand values: ");
        for (int player=0; player<state.getPlayerCount(); player++) {
            Hand hand = state.getHand(player);

            out.format("\n\tplayer %d's hand\n", player);
            out.format("\t\t %-7s %-10s %-15s %-15s\n", "slot", "card", "known colour", "known value");
            for (int slot=0; slot<hand.getSize(); slot++) {
                Card real = hand.getCard(slot);
                out.format("\t\t %-7d %-10s %-15s %-15d\n", slot, real, hand.getKnownColour(slot), hand.getKnownValue(slot));
            }
        }
    }

    public static void printDiscard(PrintStream out, GameState state) {
        out.println("discard pile:");
        Map<Card, Long> cardCounts = histogram(state.getDiscards());
        cardCounts.forEach((card, count) -> out.format("\t%s x %d\n", card, count));
    }

    public static void printDeck(PrintStream out, GameState state) {
        out.println("Deck:");
        Map<Card, Long> cardCounts = histogram(state.getDeck().toList());
        cardCounts.forEach((card, count) -> out.format("\t%10s x %-2d\n", card, count));
    }

    public static <T> Map<T,Long> histogram(Collection<T> c) {
        return c.stream().collect(Collectors.groupingBy(r -> r, Collectors.counting()));
    }

}
