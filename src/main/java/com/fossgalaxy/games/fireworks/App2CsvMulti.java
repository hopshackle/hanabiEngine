package com.fossgalaxy.games.fireworks;

import com.fossgalaxy.games.fireworks.ai.Agent;

/**
 * A runner capable of playing the games for every legal hand size
 *
 */
public class App2CsvMulti {
	private static final String[] AGENT_NAMES = {"pure_random", "random", "internal", "outer", "cautious"};
	
	public static void main(String[] args) {
		
		int runCount = App2Csv.DEFAULT_NUM_RUNS;
		
		//allow setting of run count via env variable
		String runCountEnv = System.getenv("FIREWORKS_RUN_COUNT");
		if (runCountEnv != null) {
			runCount = Integer.parseInt(runCountEnv);
		}
		
		//agents which will be playing
		String[] agentNames = AGENT_NAMES;
		String envAgents = System.getenv("FIREWORKS_AGENTS");
		if (envAgents != null) {
			agentNames = envAgents.split(",");
		}
		
		System.out.println("name,players,information,lives,moves,score");
		for (int run=0; run<runCount; run++) {
			
			for (int i=2; i<=5; i++) {
				Agent[] agents = new Agent[i];
				
				for (String name : agentNames) {
					
					//populate list of agents
					for (int agent=0; agent<agents.length; agent++) {
						agents[agent] = App.buildAgent(name);
					}
					
					App2Csv.playGame(name, agents);
				}
				
			}
		}
	}

}
