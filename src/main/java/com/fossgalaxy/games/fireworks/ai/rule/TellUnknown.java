package com.fossgalaxy.games.fireworks.ai.rule;

import java.util.Random;

import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;

/**
 * Tell the next player about an unknown card.
 */
public class TellUnknown extends AbstractTellRule {
	private Random random;
	
	public TellUnknown(){
		this.random = new Random();
	}

	@Override
	public Action execute(int playerID, GameState state) {
		if (state.getInfomation() == 0) {
			return null;
		}
		
		int nextAgent = selectPlayer(playerID, state);
		Hand otherHand = state.getHand(nextAgent);
		
		for (int slot=0; slot<state.getHandSize(); slot++) {
			Card card = otherHand.getCard(slot);
			if (card == null) {
				continue;
			}
			
			if (otherHand.getKnownColour(slot) == null) {
				return new TellColour(nextAgent, card.colour);
			}
			
			if (otherHand.getKnownValue(slot) == null) {
				return new TellValue(nextAgent, card.value);
			}
			
		}
		
		return null;
	}

}
