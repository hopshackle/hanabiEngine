package com.fossgalaxy.games.fireworks.ai.iggi;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;

public class OneStepRollout implements Agent {
    private Simulator simulator;

    public OneStepRollout() {
        this.simulator = new Simulator();
    }

    @Override
    public Action doMove(int agentID, GameState state) {


        // TODO Auto-generated method stub
        return null;
    }


}
