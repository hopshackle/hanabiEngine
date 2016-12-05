package com.fossgalaxy.games.fireworks.ai.mcts;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.iggi.Utils;
import com.fossgalaxy.games.fireworks.ai.rule.logic.DeckUtils;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.Deck;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.Hand;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.utils.DebugUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by WebPigeon on 09/08/2016.
 */
public class MCTS implements Agent {
    public static final int DEFAULT_ITERATIONS = 50_000;
    public static final int DEFAULT_ROLLOUT_DEPTH = 18;
    public static final int DEFAULT_TREE_DEPTH_MUL = 1;
    public static final int NO_LIMIT = 100;

    protected final int roundLength;
    protected final int rolloutDepth;
    protected final int treeDepthMul;
    protected final Random random;
    protected final Logger logger = LoggerFactory.getLogger(MCTS.class);

    /**
     * Create a default MCTS implementation.
     * <p>
     * This creates an MCTS agent that has a default roll-out length of 50_000 iterations, a depth of 18 and a tree
     * multiplier of 1.
     */
    public MCTS() {
        this(DEFAULT_ITERATIONS, DEFAULT_ROLLOUT_DEPTH, DEFAULT_TREE_DEPTH_MUL);
    }

    public MCTS(int roundLength) {
        this(roundLength, DEFAULT_ROLLOUT_DEPTH, DEFAULT_TREE_DEPTH_MUL);
    }

    /**
     * Create an MCTS agent which has the parameters.
     *
     * @param roundLength
     * @param rolloutDepth
     * @param treeDepthMul
     */
    public MCTS(int roundLength, int rolloutDepth, int treeDepthMul) {
        this.roundLength = roundLength;
        this.rolloutDepth = rolloutDepth;
        this.treeDepthMul = treeDepthMul;
        this.random = new Random();
    }

    @Override
    public Action doMove(int agentID, GameState state) {
        assert !state.isGameOver() : "why are you asking me for a move?";
        MCTSNode root = new MCTSNode(
                (agentID + state.getPlayerCount() - 1) % state.getPlayerCount(),
                null,
                Utils.generateAllActions(agentID, state.getPlayerCount())
        );

        Map<Integer, List<Card>> possibleCards = DeckUtils.bindCard(agentID, state.getHand(agentID), state.getDeck().toList());
        List<Integer> bindOrder = DeckUtils.bindOrder(possibleCards);


        if (logger.isTraceEnabled()) {
            logger.trace("Possible bindings: ");
            possibleCards.forEach((slot, cards) -> logger.trace("\t {} {}", slot, DebugUtils.getHistStr(DebugUtils.histogram(cards))));

            // Guaranteed cards
            logger.trace("Guaranteed Cards");

            possibleCards.entrySet().stream()
                    .filter(x -> x.getValue().size() == 1)
                    .forEach(this::printCard);

            logger.trace("We know the value of these");
            possibleCards.entrySet().stream()
                    .filter(x -> x.getValue().stream().allMatch(y -> y.value.equals(x.getValue().get(0).value)))
                    .forEach(this::printCard);

            DebugUtils.printTable(logger, state);
        }

        for (int round = 0; round < roundLength; round++) {
            //find a leaf node
            GameState currentState = state.getCopy();
            IterationObject iterationObject = new IterationObject(agentID);

            Map<Integer, Card> myHandCards = DeckUtils.bindCards(bindOrder, possibleCards);

            Deck deck = currentState.getDeck();
            Hand myHand = currentState.getHand(agentID);
            for (int slot = 0; slot < myHand.getSize(); slot++) {
                Card hand = myHandCards.get(slot);
                myHand.bindCard(slot, hand);
                deck.remove(hand);
            }
            deck.shuffle();

            MCTSNode current = select(root, currentState, iterationObject);
            int score = rollout(currentState, agentID);
            current.backup(score);
        }

        if (logger.isTraceEnabled()) {
            logger.trace("next player's moves considerations: ");
            for (MCTSNode level1 : root.getChildren()) {
                logger.trace("{}'s children", level1.getAction());
                level1.printChildren();
            }
        }

        Action chosenOne = root.getBestNode().getAction();
        if (logger.isTraceEnabled()) {
            logger.trace("Move Chosen by {} was {}", agentID, chosenOne);
            root.printChildren();
        }
        return chosenOne;
    }

    protected MCTSNode select(MCTSNode root, GameState state, IterationObject iterationObject) {
        MCTSNode current = root;
        int treeDepth = (state.getPlayerCount() * treeDepthMul) + 1;
        while (!state.isGameOver() && current.getDepth() < treeDepth) {
            MCTSNode next;
            if (current.fullyExpanded(state)) {
                next = current.getUCTNode(state);
            } else {
                next = expand(current, state);
                return next;
            }
            if (next == null) {
                //XXX if all follow on states explored so far are null, we are now a leaf node
                return current;
            }
            current = next;

            int agent = current.getAgent();
            int lives = state.getLives();
            int score = state.getScore();

            Action action = current.getAction();
            if (action != null) {
                action.apply(agent, state);
            }

            if (iterationObject.isMyGo(agent)) {
                if (state.getLives() < lives) {
                    iterationObject.incrementLivesLostMyGo();
                }
                if (state.getScore() > score) {
                    iterationObject.incrementPointsGainedMyGo();
                }
            }
        }
        return current;
    }

    /**
     * Select a new action for the expansion node.
     *
     * @param state   the game state to travel from
     * @param agentID the AgentID to use for action selection
     * @param node    the Node to use for expansion
     * @return the next action to be added to the tree from this state.
     */
    protected Action selectActionForExpand(GameState state, MCTSNode node, int agentID) {
        Collection<Action> legalActions = node.getLegalMoves(state, agentID);
        if (legalActions.isEmpty()) {
            return null;
        }

        Iterator<Action> actionItr = legalActions.iterator();

        int selected = random.nextInt(legalActions.size());
        Action curr = actionItr.next();
        for (int i = 0; i < selected; i++) {
            curr = actionItr.next();
        }

        return curr;
    }

    protected MCTSNode expand(MCTSNode parent, GameState state) {
        int nextAgentID = (parent.getAgent() + 1) % state.getPlayerCount();
        Action action = selectActionForExpand(state, parent, nextAgentID);
        // It is possible it wasn't allowed
        if (action == null) {
            return parent;
        }
        if (parent.containsChild(action)) {
            // return the correct node instead
            return parent.getChild(action);
        }
        //XXX we may expand a node which we already visited? :S
        MCTSNode child = new MCTSNode(parent, nextAgentID, action, Utils.generateAllActions(nextAgentID, state.getPlayerCount()));
        parent.addChild(child);
        return child;
    }

    protected Action selectActionForRollout(GameState state, int playerID) {
        Collection<Action> legalActions = Utils.generateActions(playerID, state);
        assert !legalActions.isEmpty() : "no legal actions in rollout";
        return legalActions.iterator().next();
    }

    protected int rollout(GameState state, final int agentID) {

        int playerID = agentID;
        int moves = 0;

        while (!state.isGameOver() && moves < rolloutDepth) {
            Action action = selectActionForRollout(state, playerID);
            action.apply(playerID, state);
            playerID = (playerID + 1) % state.getPlayerCount();
            moves++;
        }
        return state.getScore();
    }

    @Override
    public String toString() {
        return "MCTS";
    }

    private void printCard(Map.Entry<Integer, List<Card>> entry) {
        logger.trace("{} : {}", entry.getKey(), entry.getValue());
    }

}
