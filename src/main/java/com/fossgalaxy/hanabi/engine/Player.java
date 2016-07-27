package com.fossgalaxy.hanabi.engine;

import java.util.List;

public interface Player {
	
	public Move getMove(List<Move> percepts);

}
