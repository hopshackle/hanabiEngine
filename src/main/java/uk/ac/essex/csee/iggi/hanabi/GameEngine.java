package uk.ac.essex.csee.iggi.hanabi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEngine {

	public void playGame(Player ... players) {
		Hanabi hanabi = new Hanabi(players.length, 5);

        // Map of player to all moves since player's last turn
        Map<Player, List<Move>> percepts = new HashMap<>();
        for(Player player : players){
            percepts.put(player, new ArrayList<Move>());
        }
        while(!hanabi.isOver()) {
            for (Player player : players) {
                Move move = player.getMove(percepts.get(player));
                // Clear out the information in the percepts list for this player
                percepts.get(player).clear();
                switch (move.getType()) {
                    case DISCARD:
                    case PLAY:
                    case TELL:
                }

                for(Player other : players){
                    // TODO Decide if we should exclude our own move?
                    percepts.get(other).add(move);
                }

            }
        }
	}

}
