package com.fossgalaxy.games.fireworks.ai;

import com.fossgalaxy.games.fireworks.players.Player;
import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;
import com.fossgalaxy.games.fireworks.ai.mcts.*;
import com.fossgalaxy.games.fireworks.utils.HasGameOverProcessing;

import java.util.Objects;

/**
 * Wrapper for an agent policy to player.
 * <p>
 * This keeps track of state on behalf of the policy so that the agent can implement the standard interface generally
 * used by game playing agents (get a state, return an action).
 */
public class AgentPlayer implements Player {
    private final String name;
    protected final Agent policy;

    protected GameState state;
    private int playerID;

    /**
     * Create a player with a given name and policy.
     *
     * @param name   the name of this player
     * @param policy the policy this player should use
     */
    public AgentPlayer(String name, Agent policy) {
        this.name = Objects.requireNonNull(name);
        this.policy = Objects.requireNonNull(policy);

        //set the player as not currently playing a game
        this.playerID = -1;
        this.state = null;
    }

    @Override
    public Action getAction() {
        return policy.doMove(playerID, state);
    }

    @Override
    public void sendMessage(GameEvent msg) {
        assert state != null : "You didn't call setID before I got a message!";
        assert msg != null : "You passed me a null message";

        msg.apply(state, playerID);
        state.addEvent(msg);
    }

    @Override
    public void setID(int id, int nPlayers) {
        assert state == null;
        assert playerID == -1;

        this.playerID = id;
        this.state = new BasicState(nPlayers);
        policy.receiveID(id);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("{name: %s, policy: %s}", name, policy);
    }

    @Override
    public void onGameOver() {
        if (policy instanceof HasGameOverProcessing)
            ((HasGameOverProcessing) policy).onGameOver(state.getScore());
    }
}
