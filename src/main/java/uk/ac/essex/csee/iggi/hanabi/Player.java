package uk.ac.essex.csee.iggi.hanabi;

import java.util.List;

public interface Player {
	
	public Move getMove(List<Move> percepts);

}
