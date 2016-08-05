package com.fossgalaxy.games.fireworks.state.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fossgalaxy.games.fireworks.TextProtocol;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.events.CardInfoColour;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

public class TellColour implements Action {
	public final int player;
	public final CardColour colour;

	public TellColour(int player, CardColour colour) {
		this.player = player;
		this.colour = colour;
	}

	@Override
	public List<GameEvent> apply(int playerID, GameState game) {

		Hand hand = game.getHand(playerID);
		List<Integer> slots = new ArrayList<Integer>();
		for (int i = 0; i < hand.getSize(); i++) {
			Card card = hand.getCard(i);
			if (card != null && colour.equals(card.colour)) {
				slots.add(i);
			}
		}

		if (slots.isEmpty()) {
			throw new RuntimeException("you cannot tell a player about a lack of cards");
		}

		int infomation = game.getInfomation();
		if (infomation <= 0) {
			throw new RuntimeException("you have no infomation left");
		}

		game.setInfomation(infomation - 1);

		GameEvent cardInfomation = new CardInfoColour(playerID, player, colour, slots);
		return Arrays.asList(cardInfomation);
	}

	@Override
	public boolean isLegal(int playerId, GameState state) {
		return state.getInfomation() < 0;
	}

	@Override
	public String toProtocol() {
		return String.format("%s %d %s", TextProtocol.ACTION_TELL_COLOUR, player, colour);
	}

}
