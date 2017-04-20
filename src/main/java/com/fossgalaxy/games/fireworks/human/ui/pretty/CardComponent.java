package com.fossgalaxy.games.fireworks.human.ui.pretty;

import com.fossgalaxy.games.fireworks.human.ui.GameView;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.Hand;

import javax.swing.*;
import java.awt.*;

/**
 * Created by webpigeon on 20/04/17.
 */
public class CardComponent extends JComponent {
    private final Hand hand;
    private final int slot;

    public CardComponent(Hand hand, int slot) {
        setPreferredSize(new Dimension(60, 90));

        this.hand = hand;
        this.slot = slot;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Card card = hand.getCard(slot);
        if (card == null) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            return;
        }

        g.setColor(GameView.getColor(card.colour));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(GameView.getColor(card.colour).darker().darker());
        g.drawRect(0, 0, getWidth(), getHeight());
    }


}
