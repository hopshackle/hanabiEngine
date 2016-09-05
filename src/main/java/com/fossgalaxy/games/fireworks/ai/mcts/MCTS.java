package com.fossgalaxy.games.fireworks.ai.mcts;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.iggi.Utils;
import com.fossgalaxy.games.fireworks.ai.rule.logic.DeckUtils;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;

import java.util.*;

/**
 * Created by WebPigeon on 09/08/2016.
 */
public class MCTS implements Agent {
    private final static int ROUND_LENGTH = 1000;
    private final static int TREE_DEPTH = 10;
    private Random random;

    public MCTS() {
        this.random = new Random();
    }

    @Override
    public Action doMove(int agentID, GameState state) {
        MCTSNode root = new MCTSNode(agentID, null);

        for (int round = 0; round < ROUND_LENGTH; round++) {
            //find a leaf node
            GameState currentState = state.getCopy();

            Map<Integer, List<Card>> possibleCards = DeckUtils.bindCard(agentID, state.getHand(agentID), state.getDeck().toList());


            MCTSNode current = select(root, currentState);
            if (current.getDepth() < TREE_DEPTH) {
                current = expand(current, currentState);
            }

            int score = rollout(state, agentID);
            current.backup(score);
        }

        return root.getBestNode().getAction();
    }

    protected MCTSNode select(MCTSNode root, GameState state) {
        MCTSNode current = root;
        while (!current.isLeaf()) {
            int agent = current.getAgent();
            Action action = current.getAction();
            if (action != null) {
                action.apply(agent, state);
            }
            current = current.getUCTNode();
            System.out.println("c "+current);
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
    protected Action selectAction(GameState state, int agentID) {
        Collection<Action> legalActions = Utils.generateActions(agentID, state);
        Iterator<Action> actionItr = legalActions.iterator();

        Action curr = null;
        int selected = random.nextInt(legalActions.size());
        for (int i = 0; i < selected; i++) {
            curr = actionItr.next();
        }
        return curr;
    }

    protected MCTSNode expand(MCTSNode parent, GameState state) {
        int nextAgentID = (parent.getAgent() + 1) % state.getPlayerCount();
        Action action = selectAction(state, nextAgentID);
        MCTSNode child = new MCTSNode(parent, nextAgentID, action);
        parent.addChild(child);
        return child;
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
