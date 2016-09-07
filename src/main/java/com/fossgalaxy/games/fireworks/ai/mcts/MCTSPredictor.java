package com.fossgalaxy.games.fireworks.ai.mcts;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.iggi.Utils;
import com.fossgalaxy.games.fireworks.ai.rule.logic.DeckUtils;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.Deck;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.Action;

import java.util.*;

/**
 * Created by WebPigeon on 09/08/2016.
 */
public class MCTSPredictor extends MCTS {
    private Agent[] agents;

    public MCTSPredictor(Agent[] others) {
        super();
        this.agents = others;
    }

    @Override
    public Action doMove(int agentID, GameState state) {
        agents[agentID] = null;
        return super.doMove(agentID, state);
    }

    /**
     * Select a new action for the expansion node.
     *
     * @param state   the game state to travel from
     * @param agentID the AgentID to use for action selection
     * @return the next action to be added to the tree from this state.
     */
    protected Action selectAction(GameState state, int agentID) {
        if (agents[agentID] == null) {
            return super.selectAction(state, agentID);
        }

        return agents[agentID].doMove(agentID, state);
    }

    protected int rollout(GameState state, final int agentID) {
        int playerID = agentID;
        while (!state.isGameOver()) {
            Action action = selectAction(state, playerID);
            action.apply(playerID, state);
            playerID = (playerID + 1) % state.getPlayerCount();
        }
        return state.getScore();
    }


}
