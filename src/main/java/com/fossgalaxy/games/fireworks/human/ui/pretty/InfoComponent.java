package com.fossgalaxy.games.fireworks.human.ui.pretty;

import com.fossgalaxy.games.fireworks.human.ui.GameView;
import com.fossgalaxy.games.fireworks.state.GameState;

import javax.swing.*;
import java.awt.*;

/**
 * Created by webpigeon on 21/04/17.
 */
public class InfoComponent extends JComponent {
    protected final GameState state;
    protected final Stroke outline = new BasicStroke(2);

    public InfoComponent(GameState state) {
        this.setPreferredSize(new Dimension(40*4 + 20, 40*2 + 20));
        this.state = state;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();

        int pad = 5;
        int radius = (width - 10 - pad * 4) / 4;
        int info = state.getInfomation();

        Graphics2D g2 = (Graphics2D)g;
        g2.translate(10, 10);
        g2.setStroke(outline);

        for (int j=0; j<2; j++) {
            for (int i = 0; i < 4; i++) {
                g2.setColor(GameView.TANGO_BLUE);

                if (info > 0) {
                    g2.fillOval(i * radius + (pad * i), j * radius + (pad * j), radius, radius);
                    g2.setColor(GameView.TANGO_BLUE.darker().darker());
                    g2.drawOval(i * radius + (pad * i), j * radius + (pad * j), radius, radius);
                } else {
                    g2.drawOval(i * radius, j * radius, radius, radius);
                }
                info--;
            }
        }

    }
}
