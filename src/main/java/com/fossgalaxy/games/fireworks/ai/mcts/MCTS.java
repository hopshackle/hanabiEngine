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
public class MCTS implements Agent {
    private final static int ROUND_LENGTH = 50_000;
    private final static int ROLLOUT_DEPTH = 9;
    private Random random;

    public MCTS() {
        this.random = new Random();
    }

    @Override
    public Action doMove(int agentID, GameState state) {
        assert !state.isGameOver() : "why are you asking me for a move?";
        MCTSNode root = new MCTSNode( (agentID+state.getPlayerCount()-1) % state.getPlayerCount(), null);

        Map<Integer, List<Card>> possibleCards = DeckUtils.bindCard(agentID, state.getHand(agentID), state.getDeck().toList());
        List<Integer> bindOrder = DeckUtils.bindOrder(possibleCards);

        GameState invarCheck = state.getCopy();

        for (int round = 0; round < ROUND_LENGTH; round++) {
            //find a leaf node
            GameState currentState = state.getCopy();

            Map<Integer, Card> myHandCards = DeckUtils.bindCards(bindOrder, possibleCards);

            Deck deck = currentState.getDeck();
            Hand myHand = currentState.getHand(agentID);
            for (int slot=0; slot<myHand.getSize(); slot++) {
                Card hand = myHandCards.get(slot);
                myHand.setCard(slot, hand);
                deck.remove(hand);
            }
            deck.shuffle();

            MCTSNode current = select(root, currentState);
            if (current.getDepth() < state.getPlayerCount()+1) {
                current = expand(current, currentState);
            }

            int score = rollout(currentState, agentID);
            current.backup(score);
        }

        assert invarCheck.getHand(agentID).equals(state.getHand(agentID)) : "state was not invariant";

        return root.getBestNode().getAction();
    }

    protected MCTSNode select(MCTSNode root, GameState state) {
        MCTSNode current = root;
        while (!current.isLeaf() && !state.isGameOver()) {
            int agent = current.getAgent();
            Action action = current.getAction();
            if (action != null) {
                action.apply(agent, state);
            }
            MCTSNode next = current.getUCTNode(state);
            if (next == null) {
                //XXX if all follow on states explored so far are null, we are now a leaf node
                return current;
            }
            current = next;
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

        assert !legalActions.isEmpty() : "no legal moves from this state";

        Action curr = actionItr.next();
        int selected = random.nextInt(legalActions.size());
        for (int i = 0; i < selected; i++) {
            curr = actionItr.next();
        }

        assert curr != null;
        return curr;
    }

    protected MCTSNode expand(MCTSNode parent, GameState state) {
        int nextAgentID = (parent.getAgent() + 1) % state.getPlayerCount();
        Action action = selectAction(state, nextAgentID);
        //XXX we may expand a node which we already visited? :S
        assert action != null : "selected a null action for expansion";
        MCTSNode child = new MCTSNode(parent, nextAgentID, action);
        parent.addChild(child);
        return child;
    }

    protected int rollout(GameState state, final int agentID) {
        int playerID = agentID;
        int moves = 0;

        while (!state.isGameOver() && moves < ROLLOUT_DEPTH) {
            Collection<Action> legalActions = Utils.generateActions(playerID, state);
            assert !legalActions.isEmpty() : "no legal actions in rollout";
            Action action = legalActions.iterator().next();
            action.apply(playerID, state);
            playerID = (playerID + 1) % state.getPlayerCount();
            moves++;
        }
        return state.getScore();
    }


}
