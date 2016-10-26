package com.fossgalaxy.games.fireworks.cluster;

import com.fossgalaxy.games.fireworks.MixedAgentGame;

/**
 * Generate matchups between an agent and itself.
 * <p>
 * Created by webpigeon on 10/10/16.
 */
public class GeneratePureGames {

    public static void main(String[] args) {
        String[] agentsUnderTest = MixedAgentGame.getAgentNames();

        //number of seeds
        String numSeedsEnv = System.getenv("FIREWORKS_NUM_SEEDS");
        int numSeeds = 10;
        if (numSeedsEnv != null) {
            numSeeds = Integer.parseInt(numSeedsEnv);
        }

        GenerateGames.printMatchups(agentsUnderTest, agentsUnderTest, numSeeds);
    }

}
