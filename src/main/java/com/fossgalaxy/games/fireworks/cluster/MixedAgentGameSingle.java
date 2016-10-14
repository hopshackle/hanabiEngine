package com.fossgalaxy.games.fireworks.cluster;

import com.fossgalaxy.games.fireworks.App;
import com.fossgalaxy.games.fireworks.App2Csv;
import com.fossgalaxy.games.fireworks.ai.Agent;

import java.util.Random;

/**
 * A runner capable of playing the games for every legal hand size
 *
 * This runner paired with
 *
 */
public class MixedAgentGameSingle {

	public static void main(String[] args) {

		int runCount = App2Csv.DEFAULT_NUM_RUNS;
		//allow setting of run count via env variable
		String runCountEnv = System.getenv("FIREWORKS_RUN_COUNT");
		if (runCountEnv != null) {
			runCount = Integer.parseInt(runCountEnv);
		}

		//arguments for script
		String agentUnderTest = args[0];
		String agentPaired = args[1];
		long seed = Long.parseLong(args[2]);

		for (int run=0; run<runCount; run++) {
			for (int nPlayers = 2; nPlayers <= 5; nPlayers++) {

				Agent[] agents = new Agent[nPlayers];
				String[] agentStr = new String[5];

				//generate agent under test
				agents[0] = App.buildAgent(agentUnderTest, 0, agentPaired, nPlayers);
				agentStr[0] = agentUnderTest;
				for (int i = 1; i < nPlayers; i++) {
					agents[i] = App.buildAgent(agentPaired);
					agentStr[i] = agentPaired;
				}

				//System.out.println("name,seed,players,information,lives,moves,score");
				App2Csv.playGameErrTrace(agentStr, seed, agents);
			}
		}
	}

}
