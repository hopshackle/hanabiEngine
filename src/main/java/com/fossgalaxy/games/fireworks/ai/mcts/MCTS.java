package com.fossgalaxy.games.fireworks.ai.mcts;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.iggi.Utils;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;

import java.util.Collection;
import java.util.List;

/**
 * Created by Piers on 09/08/2016.
 */
public class MCTS implements Agent {
    private final static int TREE_DEPTH = 10;

    @Override
    public Action doMove(int agentID, GameState state) {
        MCTSNode root = new MCTSNode();

        //simple expansion of all nodes at depth 1
        int playerID = (agentID + 1) % state.getPlayerCount();
        Collection<Action> legalActions = Utils.generateActions(agentID, state);
        for (Action action : legalActions) {
            MCTSNode node = new MCTSNode(root, action);
            GameState cloned = state.getCopy();
            action.apply(playerID, cloned);
            node.update(cloned.getScore());
        }

        return null;
    }


}
