package com.fossgalaxy.games.fireworks.human.ui.pretty;

import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.Hand;

/**
 * Created by webpigeon on 21/04/17.
 */
public class CardHinter {
    private Hand hand;
    private CardComponent[] cardComponents;

    public CardHinter(Hand hand) {
        this.hand = hand;
        this.cardComponents = new CardComponent[hand.getSize()];
    }

    public void setCard(int slot, CardComponent component) {
        cardComponents[slot] = component;
    }

    public void hoverColour(int slot) {
        Card hoverCard = hand.getCard(slot);
        if (hoverCard == null) {
            return;
        }

        CardColour colour = hoverCard.colour;
        for (int i=0; i<hand.getSize(); i++) {
            Card card = hand.getCard(i);
            if (card != null && colour.equals(card.colour)) {
                cardComponents[i].setHover(true);
            } else {
                cardComponents[i].setHover(false);
            }
        }
    }

    public void hoverValue(int slot) {
        Card hoverCard = hand.getCard(slot);
        if (hoverCard == null) {
            return;
        }

        Integer value = hoverCard.value;
        for (int i=0; i<hand.getSize(); i++) {
            Card card = hand.getCard(i);
            if (card != null && value.equals(card.value)) {
                cardComponents[i].setHover(true);
            } else {
                cardComponents[i].setHover(false);
            }
        }
    }

    public void clearHover() {
        for (CardComponent comp : cardComponents) {
            comp.setHover(false);
        }
    }

}
