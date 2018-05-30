package com.fossgalaxy.games.fireworks.ai;

import com.fossgalaxy.games.fireworks.GameRunner;
import com.fossgalaxy.games.fireworks.GameStats;
import com.fossgalaxy.games.fireworks.ai.mcts.MCTS;
import com.fossgalaxy.games.fireworks.players.Player;
import com.fossgalaxy.games.fireworks.utils.AgentUtils;
import com.fossgalaxy.stats.BasicStats;
import com.fossgalaxy.stats.StatsSummary;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Game runner for testing.
 *
 * This will run a bunch of games with your agent so you can see how it does.
 */
public class App
{

    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm:ss 'on' dd-LLL");
    public static void main( String[] args )
    {
        String policy = (args.length < 1) ? "outer" : args[0];
        int numPlayers = (args.length < 2) ? 4 : Integer.valueOf(args[1]);
        int numGames = (args.length < 3) ? 1 : Integer.valueOf(args[2]);

        runGamesAndLogResults(policy, numPlayers, numGames);
    }

    private static void runGamesAndLogResults(String agentDescriptor, int numPlayers, int numGames) {

        System.out.println("Starting run for " + agentDescriptor + " at " + dateFormat.format(ZonedDateTime.now(ZoneId.of("UTC"))));

        Random random = new Random();
        StatsSummary statsSummary = new BasicStats();

        for (int i=0; i<numGames; i++) {
   //         System.out.println("Game " + i + " starting");
            GameRunner runner = new GameRunner("test-game", numPlayers);

            //add your agents to the game
            for (int j=0; j<numPlayers; j++) {
                // the player class keeps track of our state for us...
                Agent a = AgentUtils.buildAgent(agentDescriptor);
                Player player = new AgentPlayer(agentDescriptor, a);
                if (a instanceof MCTS) ((MCTS) a).gatherStateData(true);
                runner.addPlayer(player);
            }

            GameStats stats = runner.playGame(random.nextLong());
            statsSummary.add(stats.score);
        }

        //print out the stats
        System.out.println(String.format("%s: Avg: %f, min: %f, max: %f",
                agentDescriptor,
                statsSummary.getMean(),
                statsSummary.getMin(),
                statsSummary.getMax()));
    }
}
