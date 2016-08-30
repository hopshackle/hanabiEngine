package com.fossgalaxy.games.fireworks.ai.mcts;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.iggi.Utils;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Piers on 09/08/2016.
 */
public class MCTS implements Agent {
    private final static int TREE_DEPTH = 10;
    private Random random;

    public MCTS() {
        this.random = new Random();
    }

    @Override
    public Action doMove(int agentID, GameState state) {
        MCTSNode root = new MCTSNode();

        for (int round=0; round<1000; round++) {
            //find a leaf node
            MCTSNode current = root;
            GameState currentState = state.getCopy();
            while (!current.isLeaf()) {
                int agent = current.getAgent();
                Action action = current.getAction();
                action.apply(agent, currentState);
                current = current.getBestNode();
            }

            Action action = selectAction(state, agentID);
            expand(current, action);
            int score = rollout(state, agentID);

            current.update(score);
        }

        return root.getBestNode().getAction();
    }

    /**
     * Select a new action for the expansion node.
     *
     * @param state the game state to travel from
     * @param agentID the AgentID to use for action selection
     * @return the next action to be added to the tree from this state.
     */
    protected Action selectAction(GameState state, int agentID) {
        Collection<Action> legalActions = Utils.generateActions(agentID, state);
        Iterator<Action> actionItr = legalActions.iterator();

        Action curr = null;
        int selected = random.nextInt(legalActions.size());
        for (int i=0; i<selected; i++) {
            curr = actionItr.next();
        }
        return curr;
    }


    protected int expand(MCTSNode parent, Action action) {
        int nextAgentID = (parent.getAgent() + 1) % 5; //TODO actually use number of players
        MCTSNode child = new MCTSNode(parent, nextAgentID, action);
        parent.addChild(child);
        return -1;
    }

    protected int rollout(GameState state, final int agentID) {
        int playerID = agentID;
        while (!state.isGameOver()) {
            Collection<Action> legalActions = Utils.generateActions(agentID, state);
            assert !legalActions.isEmpty() : "no legal actions in rollout";
            Action action = legalActions.iterator().next();
            action.apply(playerID, state);
            playerID = (agentID + 1) % state.getPlayerCount();
        }
        return state.getScore();
    }



}
