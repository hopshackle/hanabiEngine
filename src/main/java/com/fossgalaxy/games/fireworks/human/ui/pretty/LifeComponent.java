package com.fossgalaxy.games.fireworks.human.ui.pretty;

import com.fossgalaxy.games.fireworks.human.ui.GameView;
import com.fossgalaxy.games.fireworks.state.GameState;

import javax.sound.midi.MidiDevice;
import javax.swing.*;
import java.awt.*;

/**
 * Created by webpigeon on 21/04/17.
 */
public class LifeComponent extends InfoComponent {

    public LifeComponent(GameState state) {
        super(state);
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);

        int width = getWidth();

        int pad = 5;
        int radius = (width - 10 - pad * 4) / 4;
        int info = state.getLives();


        Graphics2D g2 = (Graphics2D)g;
        g2.translate(10, 10);
        g2.setStroke(outline);
        int j = 0;

            for (int i = 0; i < 3; i++) {
                g2.setColor(GameView.TANGO_DARK);

                if (info > 0) {
                    g2.fillOval(i * radius + (pad * i), j * radius + (pad * j), radius, radius);
                    g2.setColor(GameView.TANGO_DARK.darker().darker());
                    g2.drawOval(i * radius + (pad * i), j * radius + (pad * j), radius, radius);
                } else {
                    g2.drawOval(i * radius, j * radius, radius, radius);
                }
                info--;
            }

    }
}
