package com.fossgalaxy.games.fireworks.state;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.PlayCard;

/**
 * High level game-play tests for play action.
 */
public class TestRules {

	@Test
	public void testPlayCardValid() {
		
		int slot = 0;
		int player = 0;
		
		CardColour colour = CardColour.BLUE;
		
		Card nextCard = new Card(5, CardColour.GREEN);
		
		//setup
		GameState state = new BasicState(2, 5);
		state.setCardAt(player, slot, new Card(1, colour));
		Deck deck = state.getDeck();
		deck.add(nextCard);
		
		
		//checks for invariants
		int lives = state.getLives();
		int infomation = state.getInfomation();
		
		//check that the table is setup for that colour
		assertEquals(0, state.getTableValue(colour));
	
		//play the card
		Action play = new PlayCard(slot);
		play.apply(player, state);
		
		//check the result is as expected
		assertEquals(1, state.getTableValue(colour));
		assertEquals(lives, state.getLives());
		assertEquals(infomation, state.getInfomation());
		assertEquals(nextCard, state.getCardAt(player, slot));
	}
	
	@Test
	public void testPlayCardInvalid() {
		
		int slot = 0;
		int player = 0;
		
		CardColour colour = CardColour.BLUE;
		
		Card nextCard = new Card(5, CardColour.GREEN);
		
		//setup
		GameState state = new BasicState(2, 5);
		state.setCardAt(player, slot, new Card(4, colour));
		Deck deck = state.getDeck();
		deck.add(nextCard);
			
		//checks for invariants
		int lives = state.getLives();
		int infomation = state.getInfomation();
		
		//check that the table is setup for that colour
		assertEquals(0, state.getTableValue(colour));
	
		//play the card
		Action play = new PlayCard(slot);
		play.apply(player, state);
		
		//check the result is as expected
		assertEquals(0, state.getTableValue(colour));
		assertEquals(lives-1, state.getLives());
		assertEquals(infomation, state.getInfomation());
		assertEquals(nextCard, state.getCardAt(player, slot));
	}
	
	@Test
	public void testPlayCardInvalidEmptyDeck() {
		int slot = 0;
		int player = 0;
		
		CardColour colour = CardColour.BLUE;
		
		//setup
		GameState state = new BasicState(2, 5);
		state.setCardAt(player, slot, new Card(4, colour));
			
		//checks for invariants
		int lives = state.getLives();
		int infomation = state.getInfomation();
		
		//check that the table is setup for that colour
		assertEquals(0, state.getTableValue(colour));
	
		//play the card
		Action play = new PlayCard(slot);
		play.apply(player, state);
		
		//check the result is as expected
		assertEquals(0, state.getTableValue(colour));
		assertEquals(lives-1, state.getLives());
		assertEquals(infomation, state.getInfomation());
		assertEquals(null, state.getCardAt(player, slot));
	}
	
	@Test
	public void testPlayCardValidEmptyDeck() {
		int slot = 0;
		int player = 0;
		
		CardColour colour = CardColour.BLUE;
		
		//setup
		GameState state = new BasicState(2, 5);
		state.setCardAt(player, slot, new Card(1, colour));
			
		//checks for invariants
		int lives = state.getLives();
		int infomation = state.getInfomation();
		
		//check that the table is setup for that colour
		assertEquals(0, state.getTableValue(colour));
	
		//play the card
		Action play = new PlayCard(slot);
		play.apply(player, state);
		
		//check the result is as expected
		assertEquals(1, state.getTableValue(colour));
		assertEquals(lives, state.getLives());
		assertEquals(infomation, state.getInfomation());
		assertEquals(null, state.getCardAt(player, slot));
	}
	
	@Test
	public void testPlayCard5GivesInfomation() {	
		int slot = 0;
		int player = 0;
		
		CardColour colour = CardColour.BLUE;
		Card nextCard = new Card(5, CardColour.GREEN);
		
		//setup
		GameState state = new BasicState(2, 5);
		state.setCardAt(player, slot, new Card(5, colour));
		Deck deck = state.getDeck();
		deck.add(nextCard);
		
		//the table has a 4 of the correct colour on it
		state.setTableValue(colour, 4);
		state.setInfomation(0);
		
		//checks for invariants
		int lives = state.getLives();
		int infomation = state.getInfomation();
		
		//check that the table is setup for that colour
		assertEquals(4, state.getTableValue(colour));
	
		//play the card
		Action play = new PlayCard(slot);
		play.apply(player, state);
		
		//check the result is as expected
		assertEquals(5, state.getTableValue(colour));
		assertEquals(lives, state.getLives());
		assertEquals(infomation+1, state.getInfomation());
		assertEquals(nextCard, state.getCardAt(player, slot));
	}
	
	
	@Test
	public void testPlayCard5GivesNoInfomation() {	
		int slot = 0;
		int player = 0;
		
		CardColour colour = CardColour.BLUE;
		Card nextCard = new Card(5, CardColour.GREEN);
		
		//setup
		GameState state = new BasicState(2, 5);
		state.setCardAt(player, slot, new Card(5, colour));
		Deck deck = state.getDeck();
		deck.add(nextCard);
		
		//the table has a 4 of the correct colour on it
		state.setTableValue(colour, 4);
		state.setInfomation(state.getStartingInfomation());
		
		//checks for invariants
		int lives = state.getLives();
		int infomation = state.getInfomation();
		
		//check that the table is setup for that colour
		assertEquals(4, state.getTableValue(colour));
	
		//play the card
		Action play = new PlayCard(slot);
		play.apply(player, state);
		
		//check the result is as expected
		assertEquals(5, state.getTableValue(colour));
		assertEquals(lives, state.getLives());
		assertEquals(infomation, state.getInfomation());
		assertEquals(nextCard, state.getCardAt(player, slot));
	}
	
}
