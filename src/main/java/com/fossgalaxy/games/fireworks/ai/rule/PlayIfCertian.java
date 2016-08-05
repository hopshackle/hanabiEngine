package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.PlayCard;

/**
 * Play a card we know is 100% safe based on provided information.
 */
public class PlayIfCertian implements Rule {

	@Override
	public Action execute(int playerID, GameState state) {

		Hand myHand = state.getHand(playerID);
		for (int slot = 0; slot < state.getHandSize(); slot++) {
			CardColour c = myHand.getKnownColour(slot);
			Integer value = myHand.getKnownValue(slot);

			if (c != null) {
				int nextValue = state.getTableValue(c);
				if (nextValue + 1 == value) {
					return new PlayCard(slot);
				}
			}
		}

		return null;
	}

}
