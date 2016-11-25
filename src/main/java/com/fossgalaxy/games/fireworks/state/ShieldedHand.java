package com.fossgalaxy.games.fireworks.state;

/**
 * A wrapper to prevent accessing of complete information.
 * <p>
 * Created by webpigeon on 18/10/16.
 */
public class ShieldedHand implements Hand {
    private Hand hand;

    public ShieldedHand(Hand hand) {
        this.hand = hand;
    }

    @Override
    public void init() {

    }

    @Override
    public Card getCard(int slot) {
        return null;
    }

    @Override
    public CardColour getKnownColour(int slot) {
        return hand.getKnownColour(slot);
    }

    @Override
    public Integer getKnownValue(int slot) {
        return hand.getKnownValue(slot);
    }

    @Override
    public int getSize() {
        return hand.getSize();
    }

    @Override
    public void setCard(int slot, Card card) {
        //nope.
    }

    @Override
    public void bindCard(int slot, Card card) {
        hand.bindCard(slot, card);
    }

    @Override
    public void setKnownColour(CardColour colour, Integer[] slots) {
        //nope.
    }

    @Override
    public void setKnownValue(Integer value, Integer[] slots) {
        //nope.
    }

    @Override
    public boolean isCompletePossible(int slot, Card card) {
        //complete information is banned.
        return false;
    }

    @Override
    public boolean isPossible(int slot, Card card) {
        return hand.isPossible(slot, card);
    }

    @Override
    public int[] getPossibleValues(int slot) {
        return hand.getPossibleValues(slot);
    }

    @Override
    public CardColour[] getPossibleColours(int slot) {
        return hand.getPossibleColours(slot);
    }

    @Override
    public boolean hasColour(CardColour colour) {
        return hand.hasColour(colour);
    }

    @Override
    public boolean hasValue(Integer value) {
        return hand.hasValue(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShieldedHand that = (ShieldedHand) o;

        return hand != null ? hand.equals(that.hand) : that.hand == null;

    }

    @Override
    public int hashCode() {
        return hand != null ? hand.hashCode() : 0;
    }
}
