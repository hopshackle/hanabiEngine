package com.fossgalaxy.games.fireworks;

import com.fossgalaxy.games.fireworks.ai.Agent;

import java.util.Random;

/**
 * Created by piers on 08/11/16.
 */
public class RiskyRunner {

    public static void main(String[] args) {

        int runCount = App2Csv.DEFAULT_NUM_RUNS;

        //allow setting of run count via env variable
        String runCountEnv = System.getenv("FIREWORKS_RUN_COUNT");
        if (runCountEnv != null) {
            runCount = Integer.parseInt(runCountEnv);
        }

        System.out.println("name,threshold,1,2,3,4,seed,players,information,lives,moves,score,disqual");
        for (double threshold = 0.1; threshold <= 1.0; threshold += 0.1) {

            Random random = new Random();

            for (int i = 2; i <= 5; i++) {
                for (int run = 0; run < runCount; run++) {
                    long seed = random.nextLong();
                    Agent[] agents = new Agent[i];
                    String[] agentStr = new String[5];

                    agents[0] = App.buildAgent("iggi_risky", threshold);
                    agentStr[0] = "iggi_risky" + "," + threshold;
                    //populate list of agents
                    for (int agent = 1; agent < agents.length; agent++) {
                        agents[agent] = App.buildAgent("iggi_risky", threshold);
                        agentStr[agent] = "iggi";
                    }

                    App2Csv.playGame(agentStr, seed, agents);
                }
            }
        }
    }
}
