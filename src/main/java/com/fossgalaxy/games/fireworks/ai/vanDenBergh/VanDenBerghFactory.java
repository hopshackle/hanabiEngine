package com.fossgalaxy.games.fireworks.ai.vanDenBergh;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.rule.ProductionRuleAgent;
import com.fossgalaxy.games.fireworks.ai.rule.random.PlayProbablySafeCard;

/**
 * Created by piers on 09/12/16.
 */
public class VanDenBerghFactory {

    private VanDenBerghFactory() {

    }

    public static Agent buildAgent() {
        ProductionRuleAgent pra = new ProductionRuleAgent();

        // If there is a card in my hand of which I am "certain enough" that ic an be played, I play it
        pra.addRule(new PlayProbablySafeCard());

        // Otherwise if there is a card in my hand of which I am "certain enough" that it is useless, I discard it

        // Otherwise if there is a hint token available, I give a hint

        // Otherwise I discard a card

        return pra;
    }
}
