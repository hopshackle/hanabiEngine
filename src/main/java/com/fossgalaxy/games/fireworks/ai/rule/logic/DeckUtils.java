package com.fossgalaxy.games.fireworks.ai.rule.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;

public class DeckUtils {
	
	public static Map<Integer, List<Card>> bindCard(int player, Hand hand, List<Card> deck) {
		
		Map<Integer, List<Card>> possible = new HashMap<>();
		
		for (Card card : deck) {
			
			for (int slot=0; slot<hand.getSize(); slot++) {
				List<Card> possibleCards = possible.get(slot);
				if (possibleCards == null) {
					possibleCards = new ArrayList<>();
					possible.put(slot, possibleCards);
				}
				
				if (hand.isPossible(slot, card)) {
					possibleCards.add(card);
				}
			}
		}	
		
		return possible;
		
	}
	
	public static boolean isDiscardable(Card card, GameState state) {
		int tableValue = state.getTableValue(card.colour);
		if (tableValue >= card.value) {
			return true;
		}
		
		//TODO factor in duplicate cards and wrecked decks
		return false;
	}
	
	public static double getProbablity(List<Card> cards, Card target) {
		double matchingCards = 0;
		for (Card card : cards) {
			if (card.equals(target)) matchingCards++;
		}
		return matchingCards/cards.size();
	}

	public static double getProbablity(List<Card> cards, CardColour target) {
		double matchingCards = 0;
		for (Card card : cards) {
			if (target.equals(card.colour)) matchingCards++;
		}
		return matchingCards/cards.size();
	}
	
	public static double getProbablity(List<Card> cards, Integer target) {
		double matchingCards = 0;
		for (Card card : cards) {
			if (target.equals(card.value)) matchingCards++;
		}
		return matchingCards/cards.size();
	}
	
}
