package com.fossgalaxy.games.fireworks.human.ui;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.state.actions.*;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

import javax.swing.*;

/**
 * Created by webpigeon on 11/04/17.
 */
public class UIPlayer extends AgentPlayer {
    private GameView view;

    /**
     * Create a player with a given name and policy.
     *
     * @param name   the name of this player
     * @param policy the policy this player should use
     */
    public UIPlayer(String name, Agent policy) {
        super(name, policy);
        this.view = new GameView(800, 600);

        JFrame frame = new JFrame();
        frame.add(view);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void setID(int id, int nPlayers) {
        super.setID(id, nPlayers);
        view.setState(state, id);
    }

    @Override
    public Action getAction() {
        return super.getAction();
    }

    @Override
    public void sendMessage(GameEvent msg) {
        super.sendMessage(msg);
        view.repaint();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
