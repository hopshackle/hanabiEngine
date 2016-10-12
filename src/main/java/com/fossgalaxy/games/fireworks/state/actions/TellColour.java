package com.fossgalaxy.games.fireworks.state.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fossgalaxy.games.fireworks.TextProtocol;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.RulesViolation;
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

		Hand hand = game.getHand(player);

		List<Integer> slots = new ArrayList<Integer>();
		for (int i = 0; i < hand.getSize(); i++) {
			Card card = hand.getCard(i);
			if (card != null && colour.equals(card.colour)) {
				slots.add(i);
			}
		}

		if (playerID == player) {
			throw new RulesViolation("you cannot tell yourself things", this);
		}

		if (slots.isEmpty()) {
			throw new RulesViolation("you cannot tell a player about a lack of cards", this);
		}

		int infomation = game.getInfomation();
		if (infomation <= 0) {
			throw new RulesViolation("you have no infomation left", this);
		}

		game.setInfomation(infomation - 1);
		hand.setKnownColour(colour, slots.toArray(new Integer[slots.size()]));

		GameEvent cardInfomation = new CardInfoColour(playerID, player, colour, slots);
		return Arrays.asList(cardInfomation);
	}

	@Override
	public boolean isLegal(int playerId, GameState state) {
		return state.getInfomation() > 0 && state.getHand(player).hasColour(colour) && playerId != player;
	}

	@Override
	public String toProtocol() {
		return String.format("%s %d %s", TextProtocol.ACTION_TELL_COLOUR, player, colour);
	}

	public String toString() {
		return String.format("tell %d about their %ss", player, colour);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TellColour that = (TellColour) o;

		if (player != that.player) return false;
		return colour == that.colour;

	}

	@Override
	public int hashCode() {
		int result = player;
		result = 31 * result + (colour != null ? colour.hashCode() : 0);
		return result;
	}
}
