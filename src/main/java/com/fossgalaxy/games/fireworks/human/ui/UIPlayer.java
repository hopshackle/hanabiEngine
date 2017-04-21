package com.fossgalaxy.games.fireworks.human.ui;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.human.ui.pretty.HumanUIAgent;
import com.fossgalaxy.games.fireworks.human.ui.pretty.PrettyGameView;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by webpigeon on 11/04/17.
 */
public class UIPlayer extends AgentPlayer {
    private GameView view;
    private JFrame frame;

    private boolean weKnowTheGameIsOver = false;
    private boolean interactive = false;

    /**
     * Create a player with a given name and policy.
     *
     * @param name   the name of this player
     * @param policy the policy this player should use
     */
    public UIPlayer(String name, Agent policy) {
        this(name, policy, false);
    }

    public UIPlayer(String name, Agent policy, boolean interactive) {
        super(name, policy);
        this.interactive = interactive;
    }


    @Override
    public Action getAction() {
        view.setPlayerMoveRequest(true);
        Action action = super.getAction();
        view.setPlayerMoveRequest(false);
        return action;
    }

    @Override
    public void setID(int id, int nPlayers) {
        super.setID(id, nPlayers);

        if (interactive) {
            this.view = new PrettyGameView(state, id, (HumanUIAgent)policy);
        } else {
            this.view = new BasicGameView(800, 600);
        }


        this.frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view);
        frame.pack();
        frame.setVisible(true);

        view.setState(state, id);
    }

    @Override
    public void sendMessage(GameEvent msg) {
        super.sendMessage(msg);

        if (view != null) {
            try {
                Thread.sleep(100l);
            } catch (InterruptedException ex) {
                //ten more mins, i'm sleepy.
            }

            view.repaint();

            if (state.isGameOver() && !weKnowTheGameIsOver) {
                JOptionPane.showMessageDialog(view, "The game is over, you scored "+state.getScore());
                weKnowTheGameIsOver = true;
            }
        }
    }

    @Override
    public void onGameOver() {
        JOptionPane.showMessageDialog(view, "The game is over, you scored "+state.getScore());
        weKnowTheGameIsOver = true;

        if (frame != null) {
            frame.dispose();
        }
    }
}
