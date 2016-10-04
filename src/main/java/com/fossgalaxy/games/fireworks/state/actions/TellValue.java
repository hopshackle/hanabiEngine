package com.fossgalaxy.games.fireworks.state.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fossgalaxy.games.fireworks.TextProtocol;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.RulesViolation;
import com.fossgalaxy.games.fireworks.state.events.CardInfoValue;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

public class TellValue implements Action {
	public final int player;
	public final int value;

	public TellValue(int player, int value) {
		this.player = player;
		this.value = value;
	}

	@Override
	public List<GameEvent> apply(int playerID, GameState game) {

		Hand hand = game.getHand(player);
		List<Integer> slots = new ArrayList<Integer>();
		for (int i = 0; i < hand.getSize(); i++) {
			Card card = hand.getCard(i);
			if (card != null && value == card.value) {
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

		GameEvent cardInfomation = new CardInfoValue(playerID, player, value, slots);
		return Arrays.asList(cardInfomation);
	}

	@Override
	public boolean isLegal(int playerId, GameState state) {
		return state.getInfomation() > 0 && state.getHand(player).hasValue(value) && player != playerId;
	}

	@Override
	public String toProtocol() {
		return String.format("%s %d %d", TextProtocol.ACTION_TELL_VALUE, player, value);
	}
	
	public String toString() {
		return String.format("tell %d about their %ss", player, value);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TellValue tellValue = (TellValue) o;

		if (player != tellValue.player) return false;
		return value == tellValue.value;

	}

	@Override
	public int hashCode() {
		int result = player;
		result = 31 * result + value;
		return result;
	}
}
