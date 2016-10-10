package com.fossgalaxy.games.fireworks.cluster;

import com.fossgalaxy.games.fireworks.MixedAgentGame;

import java.util.Random;

/**
 * Created by webpigeon on 10/10/16.
 */
public class GenerateGames {

    public static void main(String[] args) {
        String[] agentsUnderTest = MixedAgentGame.AGENT_NAMES;
        String[] agentsPaired = MixedAgentGame.AGENT_PAIRED;

        //number of seeds
        String numSeedsEnv = System.getenv("FIREWORKS_NUM_SEEDS");
        int numSeeds = 10;
        if (numSeedsEnv != null) {
            numSeeds = Integer.parseInt(numSeedsEnv);
        }

        Random r = new Random();

        for (int seedID = 0; seedID < numSeeds; seedID++) {
            long seed = r.nextLong();
        for (String agentUnderTest : agentsUnderTest) {
           for (String agentPaired : agentsPaired) {
                   System.out.println(String.format("%s %s %d", agentUnderTest, agentPaired, seed));
               }
           }
        }

    }

}
