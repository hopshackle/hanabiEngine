package com.fossgalaxy.games.fireworks.ai.iggi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.DiscardCard;
import com.fossgalaxy.games.fireworks.state.actions.PlayCard;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;

public class Utils {
	
	public static Collection<Action> generateActions(int playerID, GameState state){
		HashSet<Action> list = new HashSet<Action>();
		
		//TODO handle null cards
		Hand myHand = state.getHand(playerID);
		for (int slot=0; slot<myHand.getSize(); slot++) {
			list.add(new PlayCard(slot));
			if (state.getInfomation()!=state.getStartingInfomation()) {
				list.add(new DiscardCard(slot));
			}
		}
		
		//if we have no information, abort
		if (state.getInfomation() == 0) {
			return list;
		}
		
		//Legal Information Actions
		for (int player=0; player<state.getPlayerCount(); player++) {
			//can't tell self about hand
			if (player == playerID) {
				continue;
			}
			
			Hand hand = state.getHand(player);
			for (int slot=0; slot<hand.getSize(); slot++) {
				Card card = hand.getCard(slot);
				if (card != null) {
					list.add(new TellColour(player, card.colour));
					list.add(new TellValue(player, card.value));
				}
			}
		}
		
		return list;
	}
	
	public static Collection<Action> generateSuitableActions(int playerID, GameState state){
		HashSet<Action> list = new HashSet<Action>();
		
		//TODO handle null cards
		Hand myHand = state.getHand(playerID);
		for (int slot=0; slot<myHand.getSize(); slot++) {
			list.add(new PlayCard(slot));
			list.add(new DiscardCard(slot));
		}
		
		//Legal Information Actions
		for (int player=0; player<state.getPlayerCount(); player++) {
			//can't tell self about hand
			if (player == playerID) {
				continue;
			}
			
			Hand hand = state.getHand(player);
			for (int slot=0; slot<hand.getSize(); slot++) {
				Card card = hand.getCard(slot);
				if (card != null) {
					if (hand.getKnownColour(slot) == null) {
						list.add(new TellColour(player, card.colour));
					}
					
					if (hand.getKnownValue(slot) == null) {
						list.add(new TellValue(player, card.value));
					}
				}
			}
		}
		
		return list;
	}

}
