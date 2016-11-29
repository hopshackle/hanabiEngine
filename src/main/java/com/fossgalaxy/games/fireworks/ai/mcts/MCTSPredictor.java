package com.fossgalaxy.games.fireworks.ai.mcts;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;

import java.util.Arrays;

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

    public MCTSPredictor(Agent[] agents, int roundLength, int rolloutDepth, int treeDepthMul) {
        super(roundLength, rolloutDepth, treeDepthMul);
        this.agents = agents;
    }

    @Override
    public Action doMove(int agentID, GameState state) {
        agents[agentID] = null;
        return super.doMove(agentID, state);
    }

    @Override
    protected MCTSNode select(MCTSNode root, GameState state, IterationObject iterationObject) {
        MCTSNode current = root;
        int treeDepth = (state.getPlayerCount() * treeDepthMul) + 1;

        while (!state.isGameOver() && current.getDepth() < treeDepth) {
            MCTSNode next;
            if (current.fullyExpanded(state)) {
                next = current.getUCTNode(state);
            } else {
                int numChildren = current.getChildSize();
                next = expand(current, state);

                //trip if the move is illegal
                if (!next.getAction().isLegal(next.getAgent(), state)) {
                    log.println("INVALID NODE SELECTED?! ");
                }

                if (numChildren != current.getChildSize()) {
                    // It is new
                    return next;
                }
            }
            // Forward the state
            if (next == null) return current;
            current = next;

            int score = state.getScore();
            int lives = state.getLives();
            int agent = current.getAgent();
            Action action = current.getAction();
            if (action != null) {
                action.apply(agent, state);
            }

            if (iterationObject.isMyGo(agent)) {
                if (state.getLives() < lives) iterationObject.incrementLivesLostMyGo();
                if (state.getScore() > score) iterationObject.incrementPointsGainedMyGo();
            }
        }

        return current;
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
        return choice;
    }

    @Override
    protected Action selectActionForRollout(GameState state, int agentID) {
        if (agents[agentID] == null) {
            return super.selectActionForRollout(state, agentID);
        }

        return agents[agentID].doMove(agentID, state.getCopy());
    }

    @Override
    public String toString() {
        return String.format("MCTS(%s)", Arrays.toString(agents));
    }


}
