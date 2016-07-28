package com.fossgalaxy.games.fireworks.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fossgalaxy.games.fireworks.engine.Card;
import com.fossgalaxy.games.fireworks.engine.CardColour;
import com.fossgalaxy.games.fireworks.engine.Hand;

/**
 * The game state from a client's perspective.
 */
public class GameState {
	private static final int STARTING_LIVES = 3;
	private static final int STARTING_INFO = 8;
	
	private final List<Card> discards;
	private final Map<CardColour, Integer> table;
	private final Map<Integer, Hand> hands;
	private final int players;
	private final int handSize;
	
	private int lives;
	private int infomation;
	
	public GameState(int players, int handSize) {
		this.discards = new ArrayList<Card>();
		this.table = new EnumMap<>(CardColour.class);
		this.hands = new HashMap<>();
		this.players = players;
		this.handSize = handSize;
		this.lives = STARTING_LIVES;
		this.infomation = STARTING_INFO;
		init();
	}
	
	private void init(){
		for (int i=0; i<players; i++) {
			hands.put(i, new Hand(handSize));
		}
	}
	
	public void play(int player, int slot, Card old) {
		Integer currentVal = table.get(old.colour);
		currentVal = currentVal==null?0:currentVal;
		
		//check if this move was legal
		if (currentVal + 1 != old.value) {
			lives--;
			discards.add(old);
		} else {
			table.put(old.colour, old.value);
		}
		
		// the player will have drawn a new card, so we don't know about it.
		Hand hand = hands.get(player);
		hand.setKnownColour(slot, null);
		hand.setKnownValue(slot, null);
	}
	
	public void discard(int player, int slot, Card old) {
		//add this card to the discard pile
		discards.add(old);
		infomation--;
		
		// the player will have drawn a new card, so we don't know about it.
		Hand hand = hands.get(player);
		hand.setKnownColour(slot, null);
		hand.setKnownValue(slot, null);
	}
	
	public Hand getHand(int player) {
		Hand hand = hands.get(player);
		return new Hand(hand);
	}
	
	public void setInfomation(int player, int slot, Integer value, CardColour colour) {
		Hand hand = hands.get(player);
		hand.setKnownColour(slot, colour);
		hand.setKnownValue(slot, value);
	}
	
	public void tell(int player, CardColour c, int ... slots) {
		assert infomation > 0 : "You had no infomation left?!";
		Hand hand = hands.get(player);
		
		for (int slot : slots) {
			hand.setKnownColour(slot, c);
		}

		infomation--;
	}
	
	public void tell(int player, Integer val, int ... slots) {
		assert infomation > 0 : "You had no infomation left?!";
		Hand hand = hands.get(player);
		
		for (int slot : slots) {
			hand.setKnownValue(slot, val);
		}
		
		infomation--;
	}

	public int getLives() {
		return lives;
	}
	
	public int getInfomation() {
		return infomation;
	}
	
	public int getCurrCard(CardColour c) {
		Integer curr = table.get(c);
		return curr==null?0:curr;
	}

	public int getPlayerCount() {
		return players;
	}
	
	public Map<CardColour, Integer> getTable() {
		return Collections.unmodifiableMap(table);
	}

	public int getHandSize() {
		return handSize;
	}
	
}
