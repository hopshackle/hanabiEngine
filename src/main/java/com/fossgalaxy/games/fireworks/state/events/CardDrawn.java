package com.fossgalaxy.games.fireworks.state.events;

import com.fossgalaxy.games.fireworks.TextProtocol;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;

public class CardDrawn extends GameEvent {
	public final int playerId;
	public final int slotId;
	public final int cardValue;
	public final CardColour colour;

	public CardDrawn(int playerId, int slotId, CardColour colour, int cardValue) {
		super(MessageType.CARD_DRAWN);
		this.playerId = playerId;
		this.slotId = slotId;
		this.cardValue = cardValue;
		this.colour = colour;
	}

	@Override
	public void apply(GameState state) {
		state.setCardAt(playerId, slotId, new Card(cardValue, colour));
	}

	@Override
	public boolean isVisibleTo(int playerID) {
		return playerID != this.playerId;
	}

	@Override
	public String toTextProtocol() {
		return TextProtocol.drawMessage(playerId, slotId, colour, cardValue);
	}

}
