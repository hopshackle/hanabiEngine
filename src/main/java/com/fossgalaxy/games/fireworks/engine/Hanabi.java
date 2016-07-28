package com.fossgalaxy.games.fireworks.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Hanabi {
	private Deck deck;
	private int handSize;
    // Player number to list of cards in their hand
	private Hand[] players;
	private int lives;
	private int information;
	private int numPlayers;
	private Map<CardColour, Integer> table;

    /**
     * Constructor
     * @param numPlayers Number of players in the game
     * @param handSize The number of cards in each hand
     */
	public Hanabi(int numPlayers, int handSize) {
		this.deck = new Deck();
		this.players = new Hand[numPlayers];
		this.table = new HashMap<>();
		this.lives = 3;
		this.information = 8;
		this.numPlayers = numPlayers;
		this.handSize = handSize;
		
		deck.init();
		initHands();
	}
	
	private void initHands() {
		for (int i=0; i<numPlayers; i++) {
			Hand hand = new Hand(handSize);
			
			for (int slot=0; slot<handSize; slot++) {
				Card card = deck.getTopCard();
				hand.setCard(slot, card);
			}
			
			players[i] = hand;
		}
	}

    /**
     * Clones a game of Hanabi
     * @param hanabi
     */
	public Hanabi(Hanabi hanabi) {
		this.deck = new Deck(hanabi.deck);
		
		this.players = new Hand[hanabi.players.length];
		for (int i=0; i<players.length; i++) {
			this.players[i] = new Hand(hanabi.players[i]);
		}

		this.table = new TreeMap<>(hanabi.table);

		this.lives = hanabi.lives;
		this.information = hanabi.information;
	}

    /**
     * Is the game over?
     * @return boolean wether the game is over or not
     */
	public boolean isOver() {
		return lives <= 0 && deck.hasCardsLeft();
	}

    /**
     * Discard a given card
     * @param player The player that is discarding the card
     * @param card The card that the player is discarding
     * @return The card that was taken from the deck after the discard. TODO Why are we doing this?
     * TODO Why not place this directly in the hand
     */
	public Card discard(Integer player, Integer slot) {
		Hand hand = players[slot];
		
		Card nextCard = deck.getTopCard();
		hand.setCard(slot, nextCard);
		
		//TODO should we keep track of discards?
		
		if (information < 8) {
			information++;
		}
		
		return nextCard;
	}

    /**
     * Plays a card onto the table and returns the next card for your hand
     * @param player The player that is playing the card
     * @param card The card that the player is playing
     * @return The card that is going back into the hand of the player
     */
	public Card play(Integer player, int slot) {
		Hand hand = players[player];
		Card cardInSlot = hand.getCard(slot);
		assert cardInSlot != null : "played empy slot?!";
		
		//find out if this was a legal move
		//Integer topCard = table.getOrDefault(cardInSlot.colour, 0);
		Integer topCard = table.get(cardInSlot.colour);
		topCard = topCard==null ? 0 : topCard;
		
		
		if ( cardInSlot.value == topCard + 1 ) {
			table.put(cardInSlot.colour, cardInSlot.value);
		} else {
			lives--;
			//TODO play exploding sound
		}
		
		//draw a new card and put it in the player's hand
		Card newCard = deck.getTopCard();
		hand.setCard(slot, newCard);
		return newCard;
	}

    /**
     * Gets a view of the provided player's cards
     * @param player The player that we want to look at the cards of
     * @return The requested card
     */
	public Card getCard(Integer player, Integer slot) {
		return players[player].getCard(slot);
	}

    /**
     * Gets the current number of the colour that is on the table
     * @param colour The colour that we are asking about
     * @return The number of the highest card
     */
	public int getCurrentTable(CardColour colour) {
		Integer currentScore = table.get(colour);
		currentScore = currentScore == null ? 0 : currentScore;
		return currentScore;
	}

    /**
     * Returns the entire table as an unmodifiable map
     * @return The map
     */
	public Map<CardColour, Integer> getTable() {
		return Collections.unmodifiableMap(table);
	}

    /**
     * Returns the number of lives for the game left
     * @return
     */
	public int getLives() {
		return lives;
	}
	
	public Collection<Integer> tell(int teller, int told, CardColour colour) {
		Hand hand = players[told];
		
		Collection<Integer> slotsToTell = new HashSet<Integer>();
		for (int slot=0; slot<hand.getSize(); slot++) {
			Card card = hand.getCard(slot);
			if (colour.equals(card.colour)) {
				slotsToTell.add(slot);
				hand.setKnownColour(slot, colour);
			}
		}
		
		information--;
		return slotsToTell;
	}
	
	public Collection<Integer> tell(int teller, int told, Integer cardValue) {
		Hand hand = players[told];
		
		Collection<Integer> slotsToTell = new HashSet<Integer>();
		for (int slot=0; slot<hand.getSize(); slot++) {
			Card card = hand.getCard(slot);
			if (cardValue.equals(card.value)) {
				slotsToTell.add(slot);
				hand.setKnownValue(slot, cardValue);
			}
		}
		
		information--;
		return slotsToTell;
	}

	public Hand getHand(int player) {
		return players[player];
	}
}
