package com.fossgalaxy.games.fireworks.ai;

import com.fossgalaxy.games.fireworks.players.Player;
import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

import java.util.Objects;

/**
 * Wrapper for an agent policy to player.
 *
 * This keeps track of state on behalf of the policy so that the agent can implement the standard interface generally
 * used by game playing agents (get a state, return an action).
 */
public class AgentPlayer implements Player {

    private final String name;
    private final Agent policy;

    private GameState state;
    private int playerID;

    public AgentPlayer(String name, Agent policy) {
        this.name = Objects.requireNonNull(name);
        this.policy = Objects.requireNonNull(policy);

        //set the player as not currently playing a game
        this.playerID = -1;
        this.state = null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Action getAction() {
        return policy.doMove(playerID, state);
    }

    @Override
    public void sendMessage(GameEvent msg) {
        assert msg != null;
        msg.apply(state);
        policy.receiveEvent(msg);
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
    public String toString() {
        return String.format("{name: %s, policy: %s}", name, policy);
    }

}
