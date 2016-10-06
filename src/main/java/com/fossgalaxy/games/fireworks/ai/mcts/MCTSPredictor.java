package com.fossgalaxy.games.fireworks.ai.mcts;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.iggi.Utils;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by WebPigeon on 09/08/2016.
 */
public class MCTSPredictor extends MCTS {
    private Agent[] agents;

    public MCTSPredictor(Agent[] others) {
        super();
        this.agents = others;
    }

    public MCTSPredictor(Agent[] agents, int roundLength) {
        super(roundLength);
        this.agents = agents;
    }

    public MCTSPredictor(Agent[] agents, int roundLength, int rolloutDepth) {
        super(roundLength, rolloutDepth);
        this.agents = agents;
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
    @Override
    protected Action selectActionForExpand(GameState state, MCTSNode node, int agentID) {
        if (agents[agentID] == null) {
            return super.selectActionForExpand(state, node, agentID);
        }

        Action choice = agents[agentID].doMove(agentID, state.getCopy());
        return node.containsChild(choice) ? null : choice;
    }

    private Action selectActionForRollout(GameState state, int agentID){
        if(agents[agentID] == null){
            // Random
            Collection<Action> legalActions = Utils.generateActions(agentID, state);
            Iterator<Action> actionItr = legalActions.iterator();

            int selected = random.nextInt(legalActions.size());
            Action curr = actionItr.next();
            for (int i = 0; i < selected; i++) {
                curr = actionItr.next();
            }

            return curr;
        }
        return agents[agentID].doMove(agentID, state.getCopy());
    }


    @Override
    protected int rollout(GameState state, final int agentID) {
        int playerID = agentID;
        while (!state.isGameOver()) {
            Action action = selectActionForRollout(state, playerID);
            action.apply(playerID, state);
            playerID = (playerID + 1) % state.getPlayerCount();
        }
        return state.getScore();
    }


}
