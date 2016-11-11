package com.fossgalaxy.games.fireworks.state.events;

import com.fossgalaxy.games.fireworks.TextProtocol;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CardInfoColour extends GameEvent {
    private final static String CARD_FORMAT = "player %d has %s cards, in slot(s) %s.";

    private final int performer;
    private final int playerId;
    private final CardColour colour;
    private final Integer[] slots;

    public CardInfoColour(int performer, int playerId, CardColour colour, Collection<Integer> slotsList) {
        super(MessageType.CARD_INFO_COLOUR);
        this.performer = performer;
        this.playerId = playerId;
        this.colour = colour;

        Integer[] slots = new Integer[slotsList.size()];
        slotsList.toArray(slots);

        this.slots = slots;
    }

    public CardInfoColour(int performer, int playerId, CardColour colour, Integer... slots) {
        super(MessageType.CARD_INFO_COLOUR);
        this.performer = performer;
        this.playerId = playerId;
        this.colour = colour;
        this.slots = slots;
    }

    @Override
    public void apply(GameState state) {
        assert state.getInfomation() > 0 : "got told infomation with no infomation left?!";

        Hand playerHand = state.getHand(playerId);
        playerHand.setKnownColour(colour, slots);
        state.setInfomation(state.getInfomation() - 1);

        assert state.getInfomation() >= 0 : "negative infomation happend?!";
    }

    @Override
    public String toString() {
        return String.format(CARD_FORMAT, playerId, colour, Arrays.toString(slots));
    }

    @Override
    public String toTextProtocol() {
        // I've not gone mad, Arrays.asList returns List<int[]> x.x
        List<Integer> slotList = new ArrayList<Integer>();
        for (int slot : slots) {
            slotList.add(slot);
        }

        return TextProtocol.tellPlayer(performer, playerId, colour, slotList);
    }

    public int getPerformer() {
        return performer;
    }

    public int getPlayerId() {
        return playerId;
    }

    public CardColour getColour() {
        return colour;
    }

    public Integer[] getSlots() {
        return slots;
    }
}
