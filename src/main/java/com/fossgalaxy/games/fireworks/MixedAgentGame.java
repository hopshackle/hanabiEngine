package com.fossgalaxy.games.fireworks;

import com.fossgalaxy.games.fireworks.ai.Agent;

import java.util.Random;

/**
 * A runner capable of playing the games for every legal hand size
 *
 * This runner paired with
 *
 */
public class MixedAgentGame {
	private static final String[] AGENT_PAIRED = {"pure_random", "internal", "outer", "legal_random", "cautious"};
	private static final String[] AGENT_NAMES = {"cautious", "legal_random", "mcts", "predictorMCTS"};

	public static String[] getAgentNames() {
		String[] agentNames = AGENT_NAMES;
		String envAgents = System.getenv("FIREWORKS_AGENTS");
		if (envAgents != null) {
			agentNames = envAgents.split(",");
		}
		return agentNames;
	}

	public static String[] getPartnerNames() {
		String[] agentNames = AGENT_PAIRED;
		String envAgents = System.getenv("FIREWORKS_AGENTS_PAIRED");
		if (envAgents != null) {
			agentNames = envAgents.split(",");
		}
		return agentNames;
	}

	public static void main(String[] args) {
		
		int runCount = App2Csv.DEFAULT_NUM_RUNS;
		
		//allow setting of run count via env variable
		String runCountEnv = System.getenv("FIREWORKS_RUN_COUNT");
		if (runCountEnv != null) {
			runCount = Integer.parseInt(runCountEnv);
		}
		

		String[] agentNames = getAgentNames();
		String[] agentsPaired = getPartnerNames();

		Random random = new Random();
		
		System.out.println("name,seed,players,information,lives,moves,score");
		for (String paired : agentsPaired) {
			for (int i = 2; i <= 5; i++) {
				for (int run = 0; run < runCount; run++) {

					long seed = random.nextLong();
					Agent[] agents = new Agent[i];
					String[] agentStr = new String[5];

					for (String name : agentNames) {

						//populate list of agents
						agents[0] = App.buildAgent(name,0,paired, i);
						agentStr[0] = name;
						for (int agent = 1; agent < agents.length; agent++) {
							agents[agent] = App.buildAgent(paired);
							agentStr[agent] = paired;
						}

						App2Csv.playGame(agentStr, seed, agents);
					}

				}
			}
		}
	}

}
