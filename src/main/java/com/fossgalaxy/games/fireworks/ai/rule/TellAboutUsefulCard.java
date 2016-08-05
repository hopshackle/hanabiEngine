package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;

/**
 * Tell the next player about useful cards in their hand.
 */
public class TellAboutUsefulCard extends AbstractRule {

	@Override
	public Action execute(int playerID, GameState state) {
		if (state.getInfomation() == 0) {
			return null;
		}
		
		int nextPlayer = (playerID+1) % state.getPlayerCount();
		Hand hand = state.getHand(nextPlayer);
		
		for (int slot=0; slot<state.getHandSize(); slot++) {
			
				Card card = hand.getCard(slot);
				if (card == null) {
					continue;
				}
				
				int currTable = state.getTableValue(card.colour);
				if (card.value != currTable + 1) {
					continue;
				}
				
				if (hand.getKnownValue(slot) == null) {
					return new TellValue(nextPlayer, card.value);
				} else if (hand.getKnownColour(slot) == null) {
					return new TellColour(nextPlayer, card.colour);
				}
		}
		
		return null;
	}

}
