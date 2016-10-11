package com.fossgalaxy.games.fireworks.cluster;

import com.fossgalaxy.games.fireworks.MixedAgentGame;

import java.util.Random;

/**
 * Generate matchups between an agent and other agents.
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

        printMatchups(agentsUnderTest, agentsPaired, numSeeds);
    }

    static void printMatchups(String[] agentsUnderTest, String[] agentsPaired, int numSeeds) {

        //allow generation of known seeds (useful for comparisons between pure and mixed games)
        Random r;
        String metaSeed = System.getenv("FIREWORKS_META_SEED");
        if (metaSeed != null) {
            r = new Random(Long.parseLong(metaSeed));
        } else {
            r = new Random();
        }

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
