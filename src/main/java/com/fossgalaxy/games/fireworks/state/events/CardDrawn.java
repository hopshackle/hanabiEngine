package com.fossgalaxy.games.fireworks.state.events;

import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.Deck;
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
        Card card = new Card(cardValue, colour);

        state.setCardAt(playerId, slotId, card);

        Deck deck = state.getDeck();
        deck.remove(card);
    }

    @Override
    public boolean isVisibleTo(int playerID) {
        return playerID != this.playerId;
    }

    @Override
    public String toString() {
        return String.format("player %s draw card %s %d in slot %d", playerId, colour, cardValue, slotId);
    }

}
