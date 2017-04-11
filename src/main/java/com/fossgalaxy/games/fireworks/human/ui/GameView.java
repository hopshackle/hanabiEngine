package com.fossgalaxy.games.fireworks.human.ui;

import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;

import javax.swing.*;
import java.awt.*;

/**
 * Created by piers on 11/04/17.
 */
public class GameView extends JComponent {

    public static final int MARGIN = 15;
    private final int width, height;
    private final Object _state = new Object();
    private GameState state;

    public GameView(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        graphics.setColor(Color.CYAN);
        graphics.fillRect(0, 0, width/2, height);
        if (state != null) {
            synchronized (_state) {
                int width = cardWidth();
                for (int i = 0; i < getNumCards(); i++) {
                    // Obtain details about card
                    int player = i / state.getHandSize();
                    int slot = i % state.getHandSize();
                    Color color = getColor(state.getHand(player).getKnownColour(slot));
                    graphics.setColor(color);

                    // Draw rectangle
                    int x = (i * (width + MARGIN)) + MARGIN;
                    graphics.fillRect(x, 10, width, width * 3 / 2);
                }
            }
        }
    }

    private Color getColor(CardColour colour) {
        if (colour == null) {
            return Color.BLACK;
        }
        switch (colour) {
            case BLUE:
                return Color.BLUE;
            case RED:
                return Color.RED;
            case WHITE:
                return Color.WHITE;
            case ORANGE:
                return Color.YELLOW;
            case GREEN:
                return Color.GREEN;
            default:
                return Color.BLACK;
        }
    }

    private int cardWidth() {
        int cards = getNumCards();
        int margin = cards * MARGIN;
        return (width - margin) / cards;
    }

    private int getNumCards() {
        return state.getPlayerCount() * state.getHandSize();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    public void setState(GameState state) {
        synchronized (_state) {
            System.out.println("State being set");
            this.state = state;
        }
        this.repaint();
    }
}
