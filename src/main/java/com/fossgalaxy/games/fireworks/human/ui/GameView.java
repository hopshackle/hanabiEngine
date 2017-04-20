package com.fossgalaxy.games.fireworks.human.ui;

import com.fossgalaxy.games.fireworks.state.CardColour;
import com.fossgalaxy.games.fireworks.state.GameState;

import javax.swing.*;
import java.awt.*;

/**
 * Created by webpigeon on 20/04/17.
 */
public abstract class GameView extends JComponent {
    public abstract void setState(GameState state, int id);

    public static Color getColor(CardColour colour) {
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
}
