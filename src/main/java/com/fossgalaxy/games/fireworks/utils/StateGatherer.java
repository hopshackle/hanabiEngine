package com.fossgalaxy.games.fireworks.utils;

import com.fossgalaxy.games.fireworks.ai.rule.logic.DeckUtils;
import com.fossgalaxy.games.fireworks.state.*;

import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.google.gson.*;

public class StateGatherer {

    private static EntityLog logFile = new EntityLog("Hopshackle");
    private boolean debug = false;
    private String fileLocation = "C://simulation/hanabi";
    private GsonBuilder builder = new GsonBuilder();
    private Gson gson = builder.create();

    private List<Map<String, Double>> experienceData = new ArrayList();

    public void storeData(GameState gameState, int playerID) {
        long startTime = System.currentTimeMillis();
        Map<String, Double> newTuple = new HashMap();
        experienceData.add(newTuple);
        newTuple.put("SCORE", gameState.getScore() / 25.0);
        newTuple.put("INFORMATION", gameState.getInfomation() / (double) gameState.getStartingInfomation());
        newTuple.put("LIVES", gameState.getLives() / (double) gameState.getStartingLives());
        double cardsInStartingDeck = 50 - gameState.getPlayerCount() * gameState.getHandSize();
        // size of deck included the active player's cards
        newTuple.put("DECK_LEFT", (gameState.getDeck().getCardsLeft() - gameState.getHandSize()) / cardsInStartingDeck);
        newTuple.put("FIVES_DISCARDED", numberOfSuitsWithDiscardedFive(gameState) / 5.0);
        newTuple.put("FOURS_DISCARDED", numberOfSuitsWithDiscardedFour(gameState) / 5.0);

        for (int featureID = 0; featureID < gameState.getPlayerCount(); featureID++) {
            int featurePlayer = (featureID + playerID) % gameState.getPlayerCount();
            Hand playerHand = gameState.getHand(featurePlayer);
            if (debug) logFile.log(playerHand.toString());
            List<Card> possibles = gameState.getDeck().toList();
            if (featurePlayer != playerID)
                IntStream.range(0, playerHand.getSize())
                        .mapToObj(playerHand::getCard)
                        .filter(Objects::nonNull)
                        .forEach(possibles::add);
            // we need to add the actual cards in the player's hand to those they thiink they might have

            Map<Integer, List<Card>> possibleCards = DeckUtils.bindBlindCard(featurePlayer, playerHand, possibles);
            //         Map<Integer, List<Card>> actualCards = DeckUtils.bindCard(featurePlayer, playerHand, gameState.getDeck().toList());
            // this provides us with all possible values for the cards in hand, from the perspective of that player
            // so we can now go through this to calculate the probability of playable / discardable
            double[] maxPlayable = new double[3];
            double[] maxDiscardable = new double[3];
            for (int slot : possibleCards.keySet()) {
                StringBuilder output = new StringBuilder();
                if (debug) {
                    if (featurePlayer != playerID)
                        output.append("[" + playerHand.getCard(slot).toString() + "]\t");
                    possibleCards.get(slot).stream().forEach(c -> output.append(c.toString()));
                }
                int playable = 0;
                int discardable = 0;
                for (Card c : possibleCards.get(slot)) {
                    if (gameState.getTableValue(c.colour) == c.value - 1)
                        playable++;
                    else if (gameState.getTableValue(c.colour) >= c.value)
                        discardable++;
                    else if (c.value > 2 && allCardsDiscarded(c.value - 1, c.colour, gameState.getDiscards()))
                        discardable++;
                    else if (c.value > 3 && allCardsDiscarded(c.value - 2, c.colour, gameState.getDiscards()))
                        discardable++;
                    else if (c.value > 4 && allCardsDiscarded(c.value - 3, c.colour, gameState.getDiscards()))
                        discardable++;
                }
                double totalCards = possibleCards.get(slot).size();
                double playableProb = playable / totalCards;
                double discardableProb = discardable / totalCards;
                if (debug)
                    logFile.log(String.format("Player %d, Slot %d, Play %1.2f, Discard %1.2f: %s", featurePlayer, slot, playableProb, discardableProb, output));
                updateOrder(maxPlayable, playableProb);
                updateOrder(maxDiscardable, discardableProb);
            }
            newTuple.put(featureID + "_PLAYABLE_1", maxPlayable[0]);
            newTuple.put(featureID + "_PLAYABLE_2", maxPlayable[1]);
            newTuple.put(featureID + "_PLAYABLE_3", maxPlayable[2]);
            newTuple.put(featureID + "_DISCARDABLE_1", maxPlayable[0]);
            newTuple.put(featureID + "_DISCARDABLE_2", maxPlayable[1]);
            newTuple.put(featureID + "_DISCARDABLE_3", maxPlayable[2]);
            newTuple.put(featureID + "_PLAYING", 1.0);
            if (debug)
                logFile.log(String.format("Player %d, Playable: %1.2f/%1.2f/%1.2f\tDiscardable: %1.2f/%1.2f/%1.2f",
                        featurePlayer, maxPlayable[0], maxPlayable[1], maxPlayable[2], maxDiscardable[0], maxDiscardable[1], maxDiscardable[2]));
        }

     //   logFile.log(String.format("Total feature analysis time was %d milliseconds", System.currentTimeMillis() - startTime));

    }


    private boolean allCardsDiscarded(int value, CardColour colour, Collection<Card> discardPile) {
        int possibleCards = 2;
        if (value == 4)
            possibleCards = 1;
        if (value == 3)
            possibleCards = 3;
        for (Card discard : discardPile) {
            if (discard.colour == colour) {
                if (discard.value == value) {
                    possibleCards--;
                }
            }
            if (possibleCards < 1)
                return true;
        }
        return false;
    }

    private void updateOrder(double[] orderedArray, double newValue) {
        if (newValue > orderedArray[0]) {
            orderedArray[2] = orderedArray[1];
            orderedArray[1] = orderedArray[0];
            orderedArray[0] = newValue;
        } else if (newValue > orderedArray[1]) {
            orderedArray[2] = orderedArray[1];
            orderedArray[1] = newValue;
        } else if (newValue > orderedArray[2]) {
            orderedArray[2] = newValue;
        }
    }

    private int numberOfSuitsWithDiscardedFive(GameState state) {
        int retValue = 0;
        for (Card c : state.getDiscards()) {
            if (c.value == 5) retValue++;
        }
        return retValue;
    }

    private int numberOfSuitsWithDiscardedFour(GameState state) {
        Map<CardColour, Integer> tracker = new HashMap();
        for (Card c : state.getDiscards()) {
            if (c.value == 4) {
                tracker.put(c.colour, tracker.getOrDefault(c.colour, 0) + 1);
            }
        }
        return (int) tracker.values().stream().filter(i -> i == 2).count();
    }


    public void onGameOver(double finalScore) {

        try {
            FileWriter writerJSON = new FileWriter(fileLocation + "/rawData.json", true);
            FileWriter writerCSV = new FileWriter(fileLocation + "/rawData.csv", true);
            for (Map<String, Double> tuple : experienceData) {
                String csvLine = tuple.keySet().stream()
                        .map(k -> tuple.getOrDefault(k, 0.00))
                        .map(d -> String.format("%.3f", d))
                        .collect(Collectors.joining("\t"));
                double scoreGain = finalScore/25.0 - tuple.get("SCORE");
                tuple.put("RESULT", scoreGain);
                String jsonString = gson.toJson(tuple);
         //       String headerLine = tuple.keySet().stream().collect(Collectors.joining("\n"));
         //       System.out.println(headerLine);
                writerJSON.write(jsonString);
                writerCSV.write(String.format("%.3f\t%s\n", scoreGain, csvLine));
            }
            writerJSON.close();
            writerCSV.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
