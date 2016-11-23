package com.fossgalaxy.games.fireworks.ai.hat;

import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.*;

/**
 * Hat Guessing recommendation protocol.
 */
public enum Recommendation {
    PLAY_SLOT_1(new PlayCard(0), true),
    PLAY_SLOT_2(new PlayCard(1), true),
    PLAY_SLOT_3(new PlayCard(2), true),
    PLAY_SLOT_4(new PlayCard(3), true),
    DISCARD_SLOT_1(new DiscardCard(0), false),
    DISCARD_SLOT_2(new DiscardCard(1), false),
    DISCARD_SLOT_3(new DiscardCard(2), false),
    DISCARD_SLOT_4(new DiscardCard(3), false);

    public final Action recommended;
    private final boolean isPlayAction;

    Recommendation(Action recommended, boolean play) {
        this.recommended = recommended;
        this.isPlayAction = play;
    }

    public static Action encode(Recommendation recommendation, int myID, GameState state) {
        //in order to avoid rule violations, we need to make sure our
        //tell actions are legal, thus we need to use a card in the
        //hand to do this.

        //work out who we are allowed to tell
        int[] peopleWhoAreNotMe = new int[4];
        int size = 0;
        for (int i=0; i<5; i++) {
            if (i == myID) {
                continue;
            }
            peopleWhoAreNotMe[size++] = i;
        }

        if (recommendation.ordinal() < 4) {
            //first four, encode as rank (value)
            int playerID = peopleWhoAreNotMe[recommendation.ordinal()];
            int valueToUse = state.getHand(playerID).getCard(0).value;
            return new TellValue(playerID, valueToUse);
        } else {
            //second four, encode has suit (colour)
            int playerID = peopleWhoAreNotMe[recommendation.ordinal() - 4];
            CardColour colourToUse = state.getHand(playerID).getCard(0).colour;
            return new TellColour(playerID, colourToUse);
        }
    }

    public static Recommendation playSlot(int slot) {
        Recommendation[] recs = Recommendation.values();
        return recs[slot];
    }

    public static Recommendation discardSlot(int slot){
        Recommendation[] recs = Recommendation.values();
        return recs[4+slot];
    }

    public boolean isPlay() {
        return isPlayAction;
    }

    public boolean isDiscard() {
        return !isPlayAction;
    }
}
