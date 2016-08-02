package com.fossgalaxy.games.fireworks.state.actions;

import java.util.Arrays;
import java.util.List;

import com.fossgalaxy.games.fireworks.TextProtocol;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.events.CardDiscarded;
import com.fossgalaxy.games.fireworks.state.events.CardDrawn;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

public class DiscardCard implements Action {
	public final int slot;

	public DiscardCard(int slot) {
		this.slot = slot;
	}

	@Override
	public List<GameEvent> apply(int playerID, GameState game) {
		int currentInfo = game.getInfomation();
		int maxInfo = game.getStartingInfomation();
		
		if (currentInfo == maxInfo) {
			throw new RuntimeException("this is a voilation of the game rules!");
		}
		
		//deal with the old card first
		Card oldCard = game.getCardAt(playerID, slot);
		game.addToDiscard(oldCard);
		
		//the players gain one information back
		game.setInfomation(currentInfo + 1);
		
		//deal with the new card
		//XXX null pointer exception if next card was null.
		Card newCard = game.drawFromDeck();
		game.setCardAt(playerID, slot, newCard);

		return Arrays.asList(new CardDiscarded(playerID, slot, oldCard.colour, oldCard.value),
				new CardDrawn(playerID, slot, newCard.colour, newCard.value));
	}

	@Override
	public boolean isLegal(GameState state) {
		return state.getInfomation() != state.getStartingInfomation();
	}

	@Override
	public String toProtocol() {
		return String.format("%s %d", TextProtocol.ACTION_DISCARD, slot);
	}

}
