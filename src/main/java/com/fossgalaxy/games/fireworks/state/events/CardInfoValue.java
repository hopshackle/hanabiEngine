package com.fossgalaxy.games.fireworks.state.events;

import com.fossgalaxy.games.fireworks.TextProtocol;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CardInfoValue extends GameEvent {
    private final static String CARD_FORMAT = "player %d has %d cards, in slot(s) %s.";

    private final int performer;
    private final int playerId;
    private final int value;
    private final Integer[] slots;

    public CardInfoValue(int performer, int playerId, int value, Collection<Integer> slotsList) {
        super(MessageType.CARD_INFO_VALUE);
        this.performer = performer;
        this.playerId = playerId;
        this.value = value;

        Integer[] slots = new Integer[slotsList.size()];
        slotsList.toArray(slots);

        this.slots = slots;
    }

    public CardInfoValue(int performer, int playerId, int value, Integer... slots) {
        super(MessageType.CARD_INFO_VALUE);
        this.performer = performer;
        this.playerId = playerId;
        this.value = value;
        this.slots = slots;
    }

    @Override
    public void apply(GameState state) {
        Hand playerHand = state.getHand(playerId);
        playerHand.setKnownValue(value, slots);
        state.setInfomation(state.getInfomation() - 1);
    }

    @Override
    public String toString() {
        return String.format(CARD_FORMAT, playerId, value, Arrays.toString(slots));
    }

    @Override
    public String toTextProtocol() {

        // I've not gone mad, Arrays.asList returns List<int[]> x.x
        List<Integer> slotList = new ArrayList<Integer>();
        for (int slot : slots) {
            slotList.add(slot);
        }

        return TextProtocol.tellPlayer(performer, playerId, value, slotList);
    }

    public int getPerformer() {
        return performer;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getValue() {
        return value;
    }

    public Integer[] getSlots() {
        return slots;
    }
}
