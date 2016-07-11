package uk.ac.essex.csee.iggi.hanabi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Hanabi {
	private Deck deck;
	private int handSize;
    // Player number to list of cards in their hand
	private Map<Integer, List<Card>> players;
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
		this.players = new HashMap<>();
		this.table = new HashMap<>();
		this.lives = 3;
		this.information = 8;
		this.numPlayers = numPlayers;
		this.handSize = handSize;
		
		initDeck();
		initHands();
	}
	
	private void initDeck() {
		for (CardColour c : CardColour.values()) {
			for (int i=1; i<=5; i++) {
				deck.add(new Card(i, c));
				
				// there are at least 2 of every non-5 card
				if ( i < 4 ) {
					deck.add(new Card(i, c));
				}
				
				// there are are 3 ones
				if (i == 1) {
					deck.add(new Card(i, c));
				}
			}
		}
	}
	
	private void initHands() {
		for (int i=0; i<numPlayers; i++) {
			players.put(i, drawHand(handSize));
		}
	}

    /**
     * Draws a hand of given size from the deck
     * @param size The size of the hand
     * @return The list of cards retrieved from the deck
     */
	public List<Card> drawHand(int size) {
		List<Card> cards = new ArrayList<Card>();
		for (int i=0; i<size;i++) {
			cards.add(deck.getTopCard());
		}
		
		return cards;
	}

    /**
     * Clones a game of Hanabi
     * @param hanabi
     */
	public Hanabi(Hanabi hanabi) {
		this.deck = new Deck(hanabi.deck);
        //TODO  -  Check that this is a proper deep clone now
		this.players = new TreeMap<>();
		for(Integer index : hanabi.players.keySet()){
			this.players.put(index, new ArrayList<>(hanabi.players.get(index)));
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
     * Gets the indices of every card that matches the ineger provided
     *
     * For example getting the index of each card that is a 2
     * @param whoIsBeingTold The player that is being told the information about
     * @param number The number that is in the query
     * @return The list of indices of each card that matches the query. Will be null if the query isn't allowed
     * and empty if there were no cards matching the query
     */
	public List<Integer> getAllInteger(Integer whoIsBeingTold, Integer number) {
		if (information < 0) {
			return null; //nope
		}
		
		List<Integer> cardPos = new ArrayList<Integer>();
		
		assert players.containsKey(whoIsBeingTold);
		
		List<Card> playerBeingToldCards = players.get(whoIsBeingTold);
		for (int i = 0; i<playerBeingToldCards.size(); i++){
			Card card = playerBeingToldCards.get(i);
			if (card.value == number) {
				cardPos.add(i);
			}
		}
		
		information--;
		
		return cardPos;
	}

    /**
     * Gets the indices of every card that matches the CardColour provided
     *
     * For example getting the index of each card that is a White
     * @param whoIsBeingTold The player that is being told the information about
     * @param colour The CardColour that is in the query
     * @return The list of indices of each card that matches the query. Will be null if the query isn't allowed
     * and empty if there were no cards matching the query
     */
	public List<Integer> getAllColour(Integer whoIsBeingTold, CardColour colour) {
		if (information < 0) {
			return null; //nope
		}
		
		List<Integer> cardPos = new ArrayList<Integer>();
		
		assert players.containsKey(whoIsBeingTold);
		
		List<Card> playerBeingToldCards = players.get(whoIsBeingTold);
		for (int i = 0; i<playerBeingToldCards.size(); i++){
			Card card = playerBeingToldCards.get(i);
			if (card.colour == colour) {
				cardPos.add(i);
			}
		}
		
		information--;
		
		return cardPos;
	}

    /**
     * Gets the next card
     *
     * TODO what was this function for?
     * @param player
     * @param card
     * @return
     */
	public Card getNextCard(Integer player, Card card) {
		List<Card> cards = players.get(player);
		assert players.containsKey(player) : "player does not exist";
		assert cards.contains(card) : "Player is cheating!";
		cards.remove(card);
		
		return deck.getTopCard();
	}

    /**
     * Discard a given card
     * @param player The player that is discarding the card
     * @param card The card that the player is discarding
     * @return The card that was taken from the deck after the discard. TODO Why are we doing this?
     * TODO Why not place this directly in the hand
     */
	public Card discard(Integer player, Card card) {
		Card nextCard = getNextCard(player, card);
		
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
	public Card play(Integer player, Card card) {
		Card nextCard = getNextCard(player, card);
		
		Integer currentScore = table.get(card.colour);
		currentScore = currentScore == null ? 0 : currentScore;
		
		if (currentScore + 1 == card.value) {
			table.put(card.colour, card.value);
		} else {
			lives--;
			//TODO play exploding sound
		}
		
		return nextCard;
	}

    /**
     * Gets a view of the provided player's cards
     * @param player The player that we want to look at the cards of
     * @return The cards - an unmodifiableList
     */
	public List<Card> getCards(Integer player) {
		return Collections.unmodifiableList(players.get(player));
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
}
