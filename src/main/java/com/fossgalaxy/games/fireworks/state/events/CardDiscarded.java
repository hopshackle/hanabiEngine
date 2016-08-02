package com.fossgalaxy.games.fireworks.state.events;

import com.fossgalaxy.games.fireworks.TextProtocol;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;

public class CardDiscarded extends GameEvent {
	private final static String CARD_FORMAT = "player %d discarded slot %s, it was a %s %d.";

	private final int playerId;
	private final int slotId;
	private final int value;
	private final CardColour colour;

	public CardDiscarded(int playerId, int slotId, CardColour colour, int value) {
		super(MessageType.CARD_DISCARDED);
		this.playerId = playerId;
		this.slotId = slotId;
		this.colour = colour;
		this.value = value;
	}

	@Override
	public void apply(GameState state) {
		state.addToDiscard(new Card(value, colour));
		state.setCardAt(playerId, slotId, null);
		state.setInfomation(state.getInfomation()+1);
	}

	@Override
	public String toString() {
		return String.format(CARD_FORMAT, playerId, slotId, colour, value);
	}

	@Override
	public String toTextProtocol() {
		return TextProtocol.discardCard(playerId, slotId, new Card(value, colour));
	}

}
