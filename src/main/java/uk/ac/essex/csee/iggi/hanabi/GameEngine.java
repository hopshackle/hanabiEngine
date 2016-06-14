package uk.ac.essex.csee.iggi.hanabi;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {

	public void playGame(Player ... players) {
		Hanabi hanabi = new Hanabi(players.length, 5);
		
		for (Player player : players) {
			Move move = player.getMove(precepts);
			
			switch (move.getType()) {
				case DISCARD:
					precepts.add(hanabi)
					
				case PLAY:
				case TELL:
			}
			
		}
	}
	
	private void doMove(Move move) {
		switch(move.type) {
			case PLAY:
			case DISCARD:
			case TELL: 
		}
	}

}
