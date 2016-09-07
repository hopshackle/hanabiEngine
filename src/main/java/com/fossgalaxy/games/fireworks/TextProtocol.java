package com.fossgalaxy.games.fireworks;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.DiscardCard;
import com.fossgalaxy.games.fireworks.state.actions.PlayCard;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;

public class TextProtocol {
	public static final String ACTION_PLAY = "play";
	public static final String ACTION_DISCARD = "discard";
	public static final String ACTION_TELL_COLOUR = "tell_colour";
	public static final String ACTION_TELL_VALUE = "tell_value";

	public static final String EFFECT_DRAW = "draw";
	public static final String EFFECT_GAMEINFO = "draw";

	public static final String PROTOCOL_REQ_MOVE = "DOMOVE";

	public static String buildMessage(int player, String action, String args) {
		return player + " " + action + " " + args;
	}

	public static String discardCard(int player, int slot, Card card) {
		return buildMessage(player, ACTION_DISCARD, "" + slot + " " + card.colour + " " + card.value);
	}

	public static String drawMessage(int player, int slot, CardColour colour, int value) {
		return buildMessage(player, EFFECT_DRAW, slot + " " + colour + " " + value);
	}

	public static String gameSetup(int handSize, int players, int infomation) {
		return buildMessage(-1, EFFECT_GAMEINFO, handSize + " " + players + " " + infomation);
	}

	private static String join(Collection<Integer> args) {
		StringBuilder builder = new StringBuilder();
		Iterator<Integer> itr = args.iterator();

		while (itr.hasNext()) {
			builder.append(itr.next());
			if (itr.hasNext()) {
				builder.append(",");
			}
		}

		return builder.toString();
	}

	public static String playCard(int player, int slot, Card card) {
		return buildMessage(player, ACTION_PLAY, "" + slot + " " + card.colour + " " + card.value);
	}

	public static Action stringToAction(String move) {
		String[] moveArgs = move.split(" ");

		switch (moveArgs[0]) {
		case "play":
		case "PLAY": {
			// PLAY $SLOT
			int slot = Integer.parseInt(moveArgs[1]);
			return new PlayCard(slot);
		}
		case "discard":
		case "DISCARD": {
			// DISCARD $SLOT
			int slot = Integer.parseInt(moveArgs[1]);
			return new DiscardCard(slot);
		}
		case "tell_colour":
		case "TELL_COLOUR": {
			// TELL_COLOUR $PLAYER $COLOUR

			int player = Integer.parseInt(moveArgs[1]);
			CardColour colour = CardColour.valueOf(moveArgs[2]);
			return new TellColour(player, colour);
		}
		case "tell_value":
		case "TELL_VALUE": {
			// TELL_VALUE $PLAYER $VALUE

			int player = Integer.parseInt(moveArgs[1]);
			int value = Integer.parseInt(moveArgs[2]);
			return new TellValue(player, value);
		}
		default: {
			System.err.println("unknown action " + move + " parts: " + Arrays.toString(moveArgs));
			return null;
		}
		}
	}

	public static String tellPlayer(int player, int who, CardColour colour, Collection<Integer> slots) {
		return buildMessage(player, ACTION_TELL_COLOUR, who + " " + colour + " " + join(slots));
	}

	public static String tellPlayer(int player, int who, Integer value, Collection<Integer> slots) {
		return buildMessage(player, ACTION_TELL_VALUE, who + " " + value + " " + join(slots));
	}

}
