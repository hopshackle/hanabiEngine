package com.fossgalaxy.games.fireworks;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.ai.RandomAgent;
import com.fossgalaxy.games.fireworks.ai.iggi.IGGIFactory;
import com.fossgalaxy.games.fireworks.ai.osawa.OsawaFactory;
import com.fossgalaxy.games.fireworks.players.Player;

/**
 * Hello world!
 *
 */
public class App2Csv {
	private static final Integer DEFAULT_NUM_RUNS = 10_000;
	
	public static void main(String[] args) {
		
		int runCount = DEFAULT_NUM_RUNS;
		
		//allow setting of run count via env variable
		String runCountEnv = System.getenv("FIREWORKS_RUN_COUNT");
		if (runCountEnv != null) {
			runCount = Integer.parseInt(runCountEnv);
		}
		
		
		System.out.println("name,players,information,lives,moves,score");
		for (int run=0; run<runCount; run++) {
			playGame("pure_random", new RandomAgent(), new RandomAgent(), new RandomAgent());
			playGame("random", OsawaFactory.buildRandom(), OsawaFactory.buildRandom(), OsawaFactory.buildRandom());
			playGame("internal", OsawaFactory.buildInternalState(), OsawaFactory.buildInternalState(), OsawaFactory.buildInternalState());
			playGame("outer", OsawaFactory.buildOuterState(), OsawaFactory.buildOuterState(), OsawaFactory.buildOuterState());
			playGame("cautious", IGGIFactory.buildCautious(), IGGIFactory.buildCautious(), IGGIFactory.buildCautious());
		}
		
		//System.out.println("avg: "+sum/games);
	}
	
	public static GameStats playGame(String name, Player ... players) {
		GameRunner runner = new GameRunner(players.length);
		
		for (int i=-0; i<players.length; i++) {
			runner.addPlayer(players[i]);
			players[i].setID(i, players.length);
		}
		
		GameStats stats = runner.playGame();
		System.out.println(String.format("%s,%d,%d,%d,%d,%d", name,stats.nPlayers, stats.infomation, stats.lives, stats.moves, stats.score));
		return stats;
	}
	
	public static GameStats playGame(String name, Agent ... agents) {
		Player[] wrapper = new Player[agents.length];
		for (int i=0; i<agents.length; i++) {
			wrapper[i] = new AgentPlayer(i, agents[i]);
		}
		return playGame(name, wrapper);
	}

}
