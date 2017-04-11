package com.fossgalaxy.games.fireworks.human.ui;

import com.fossgalaxy.games.fireworks.GameRunner;
import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.utils.AgentUtils;

/**
 * Created by webpigeon on 11/04/17.
 */
public class HumanGame {

    public static void main(String[] args) {

        int nPlayers = 5;
        int human = 0;

        String pairedAgent = "pmcts";
        String pairedWith = "iggi";
        GameRunner runner = new GameRunner("human-"+System.currentTimeMillis(), nPlayers);

        for (int i=0; i<nPlayers; i++) {
            if( i != human) {
                runner.addPlayer(new AgentPlayer(pairedWith, AgentUtils.buildAgent(pairedWith)));
            } else {
                String models = pairedWith;
                for (int j=1; j<nPlayers; j++) {
                    models+= "|"+pairedWith;
                }

                runner.addPlayer(new UIPlayer("uiPlayer", AgentUtils.buildAgent(pairedAgent, models)));
            }
        }

        runner.playGame(42l);
    }
}
