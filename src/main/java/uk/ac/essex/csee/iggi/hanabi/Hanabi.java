package uk.ac.essex.csee.iggi.hanabi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Hanabi {
	private Deck deck;
	private int handSize;
	private Map<Integer, List<Card>> players;
	private int lives = 3;
	private int infomation = 8;
	private Map<CardColour, Integer> table;
	
	public Hanabi(int numPlayers, int handSize) {
		this.deck = new Deck();
		this.players = new TreeMap<>();
		this.table = new TreeMap<>();
		this.lives = 3;
		this.infomation = 8;
		this.handSize = handSize;
		
		for (int i=0; i<numPlayers; i++) {
			players.put(i, drawHand(handSize));
		}
	}
	
	public List<Card> drawHand(int size) {
		List<Card> cards = new ArrayList<Card>();
		for (int i=0; i<size;i++) {
			cards.add(deck.getTopCard());
		}
		
		return cards;
	}
	
	public Hanabi(Hanabi hanabi) {
		this.deck = new Deck(hanabi.deck);
		this.players = new TreeMap<>(hanabi.players);
		this.table = new TreeMap<>(hanabi.table);
		this.lives = hanabi.lives;
		this.infomation = hanabi.infomation;
	}

	public boolean isOver() {
		return lives <= 0 && deck.hasCardsLeft();
	}
	
	public List<Integer> getAllInteger(Integer whoIsBeingTold, Integer number) {
		if (infomation < 0) {
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
		
		infomation--;
		
		return cardPos;
	}
	
	public List<Integer> getAllColour(Integer whoIsBeingTold, CardColour colour) {
		if (infomation < 0) {
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
		
		infomation--;
		
		return cardPos;
	}
	
	public Card getNextCard(Integer player, Card card) {
		List<Card> cards = players.get(player);
		assert players.containsKey(player) : "player does not exist";
		assert cards.contains(card) : "Player is cheating!";
		cards.remove(card);
		
		return deck.getTopCard();
	}
	
	public Card discard(Integer player, Card card) {
		Card nextCard = getNextCard(player, card);
		
		if (infomation < 8) {
			infomation++;
		}
		
		return nextCard;
	}
	
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
	
	public List<Card> getCards(Integer player) {
		return Collections.unmodifiableList(players.get(player));
	}
	
	public Integer getCurrentTable(CardColour colour) {
		Integer currentScore = table.get(colour);
		currentScore = currentScore == null ? 0 : currentScore;
		return currentScore;
	}
	
	public Map<CardColour, Integer> getTable() {
		return Collections.unmodifiableMap(table);
	}
	
	public int getLives() {
		return lives;
	}
}
