package com.fossgalaxy.games.fireworks;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.players.Player;

/**
 * Hello world!
 *
 */
public class App2Csv {
	public static final Integer GAME_SIZE = 3;
	public static final Integer DEFAULT_NUM_RUNS = 10_000;
	public static final String[] AGENT_NAMES = {"pure_random", "random", "internal", "outer", "cautious"};
	
	public static void main(String[] args) {
		
		int runCount = DEFAULT_NUM_RUNS;
		
		//allow setting of run count via env variable
		String runCountEnv = System.getenv("FIREWORKS_RUN_COUNT");
		if (runCountEnv != null) {
			runCount = Integer.parseInt(runCountEnv);
		}
		
		//agents which will be playing
		String[] agentNames = App2Csv.AGENT_NAMES;
		String envAgents = System.getenv("FIREWORKS_AGENTS");
		if (envAgents != null) {
			agentNames = envAgents.split(",");
		}
		
		//play the games
		System.out.println("name,players,information,lives,moves,score");
		for (int run=0; run<runCount; run++) {
			Agent[] agents = new Agent[GAME_SIZE];
			
			for (String name : agentNames) {
				for (int agent=0; agent<agents.length; agent++) {
					agents[agent] = App.buildAgent(name);
				}
				
				playGame(name, agents);
			}
		}
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
