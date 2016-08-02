package com.fossgalaxy.games.fireworks.state.actions;

import java.util.ArrayList;
import java.util.List;

import com.fossgalaxy.games.fireworks.TextProtocol;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.events.CardDrawn;
import com.fossgalaxy.games.fireworks.state.events.CardPlayed;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

public class PlayCard implements Action {
	public final int slot;

	public PlayCard(int slot) {
		this.slot = slot;
	}

	@Override
	public List<GameEvent> apply(int playerID, GameState game) {

		//deal with the old card first
		Card oldCard = game.getCardAt(playerID, slot);
		assert oldCard != null : "old card was unknown or did not exist";
		
		//figure out the next value
		int nextValue = game.getTableValue(oldCard.colour) + 1;
		
		//check if the card was valid
		if (nextValue == oldCard.value) {
			game.setTableValue(oldCard.colour, nextValue);
			
			//if you complete a firework, you get an information back
			if (nextValue == 5) {
				int currentInfo = game.getInfomation();
				int maxInfo = game.getStartingInfomation();
				if (currentInfo < maxInfo) {
					game.setInfomation(currentInfo + 1);
				}
			}
			
		} else {
			//if this card wasn't valid, discard it and lose a life.
			game.addToDiscard(oldCard);
			game.setLives(game.getLives() - 1);
		}
		
		ArrayList<GameEvent> events = new ArrayList<>();
		events.add(new CardPlayed(playerID, slot, oldCard.colour, oldCard.value));
		
		//deal with the new card
		//XXX null pointer exception if next card was null.
		if (game.getDeck().hasCardsLeft()) {
			Card newCard = game.drawFromDeck();
			game.setCardAt(playerID, slot, newCard);
			events.add(new CardDrawn(playerID, slot, newCard.colour, newCard.value));
		}
		
		return events;
	}

	@Override
	public boolean isLegal(GameState state) {
		return true;
	}

	@Override
	public String toProtocol() {
		return String.format("%s %d", TextProtocol.ACTION_PLAY, slot);
	}

}
