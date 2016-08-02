package com.fossgalaxy.games.fireworks.state;

import java.util.Collection;
import java.util.Map;

public interface GameState {

	int STARTING_LIVES = 3;
	int STARTING_INFO = 8;

	//meta data
	int getPlayerCount();
	int getHandSize();
	int getStartingInfomation();
	int getStartingLives();
	boolean isGameOver();
	int getScore();
	
	//query the state (primatives)
	Card getCardAt(int player, int slot);
	int getLives();
	int getInfomation();
	Hand getHand(int player);
	Map<CardColour, Integer> getTable();
	Collection<Card> getDiscards();

	//update the state
	void setKnownValue(int player, int slot, Integer value, CardColour colour);
	void addToDiscard(Card card);
	void setInfomation(int newValue);
	void setLives(int newValue);
	Card drawFromDeck();
	void setCardAt(int player, int slot, Card newCard);
	void setTableValue(CardColour c, int nextValue);
}