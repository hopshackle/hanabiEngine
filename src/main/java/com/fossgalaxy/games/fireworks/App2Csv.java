package com.fossgalaxy.games.fireworks;

import java.io.*;
import java.util.Random;
import java.util.UUID;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.players.Player;

/**
 * Hello world!
 *
 */
public class App2Csv {
	public static final Integer GAME_SIZE = 3;
	public static final Integer DEFAULT_NUM_RUNS = 100;
	protected static final String[] AGENT_NAMES = {"pure_random", "internal", "outer", "legal_random", "cautious", "mcts", "cautiousMCTS"};
	
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
		
		Random random = new Random();
		
		//play the games
		System.out.println("name,seed,players,information,lives,moves,score");
		for (int run=0; run<runCount; run++) {
			Agent[] agents = new Agent[GAME_SIZE];
			
			//use the same seed for 1 game for all agents (for fairness)
			long seed = random.nextLong();
			
			for (String name : agentNames) {
				for (int agent=0; agent<agents.length; agent++) {
					agents[agent] = App.buildAgent(name);
				}
				
				playGame(name, seed, agents);
			}
		}
	}
	
	public static GameStats playGame(String name, Long seed, Player ... players) {
		UUID id = UUID.randomUUID();
		try (
				FileOutputStream fos = new FileOutputStream(String.format("trace_%s.csv", id));
				PrintStream ps = new PrintStream(fos)
		){
			GameRunner runner = new GameRunner(id, players.length, null);

			for (int i=-0; i<players.length; i++) {
				runner.addPlayer(players[i]);
				players[i].setID(i, players.length);
			}

			GameStats stats = runner.playGame(seed);
			ps.println("DEBUG,game is over");
			System.out.println(String.format("%s,%d,%d,%d,%d,%d,%d,%d", name, seed, stats.nPlayers, stats.infomation, stats.lives, stats.moves, stats.score, stats.disqal));
			return stats;
		} catch (IOException ex) {
			System.err.println("error: " + ex.toString());
		}
		return null;
	}

	public static GameStats playGameNoTrace(String name, Long seed, Player ... players) {
		UUID id = UUID.randomUUID();
		try {
			GameRunner runner = new GameRunner(id, players.length, null);

			for (int i=-0; i<players.length; i++) {
				runner.addPlayer(players[i]);
				players[i].setID(i, players.length);
			}

			GameStats stats = runner.playGame(seed);
			System.out.println(String.format("%s,%d,%d,%d,%d,%d,%d,%d", name, seed, stats.nPlayers, stats.infomation, stats.lives, stats.moves, stats.score, stats.disqal));
			return stats;
		} catch (Exception ex) {
			System.err.println("error: " + ex.toString());
		}
		return null;
	}

	public static GameStats playGameNoTrace(String name, Long seed, Agent ... agents) {
		Player[] wrapper = new Player[agents.length];
		for (int i=0; i<agents.length; i++) {
			wrapper[i] = new AgentPlayer(i, agents[i]);
		}
		return playGameNoTrace(name, seed, wrapper);
	}

	public static GameStats playGame(String name, Long seed, Agent ... agents) {
		Player[] wrapper = new Player[agents.length];
		for (int i=0; i<agents.length; i++) {
			wrapper[i] = new AgentPlayer(i, agents[i]);
		}
		return playGame(name, seed, wrapper);
	}


}
