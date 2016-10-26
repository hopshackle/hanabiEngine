package com.fossgalaxy.games.fireworks.ai.iggi;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.osawa.rules.TellPlayableCardOuter;
import com.fossgalaxy.games.fireworks.ai.rule.*;
import com.fossgalaxy.games.fireworks.ai.rule.random.DiscardRandomly;
import com.fossgalaxy.games.fireworks.ai.rule.simple.DiscardIfCertian;
import com.fossgalaxy.games.fireworks.ai.rule.simple.PlayIfCertian;

/**
 * Stratergies used/theorised about by IGGI students.
 */
public class IGGIFactory {

    private IGGIFactory() {

    }

    /**
     * Cautious but helpful bot.
     * <p>
     * This policy will only play cards it is sure about, it will discard cards it knows are useless
     * before considering discarding randomly if it has no other choice.
     *
     * @return an agent implementing the appropriate strategy
     */
    public static Agent buildCautious() {
        ProductionRuleAgent pra = new ProductionRuleAgent();
        pra.addRule(new PlayIfCertian());
        pra.addRule(new PlaySafeCard());
        pra.addRule(new TellAnyoneAboutUsefulCard());
        pra.addRule(new DiscardIfCertian());
        pra.addRule(new DiscardRandomly());
        return pra;
    }

    public static Agent buildIGGIPlayer() {
        ProductionRuleAgent pra = new ProductionRuleAgent();
        pra.addRule(new PlayIfCertian());
        pra.addRule(new PlaySafeCard());
        pra.addRule(new TellAnyoneAboutUsefulCard());
        pra.addRule(new DiscardIfCertian());
        pra.addRule(new DiscardOldestFirst());

        return pra;
    }

    public static Agent buildTest2() {
        ProductionRuleAgent pra = new ProductionRuleAgent();
        pra.addRule(new TellPlayableCardOuter());
        pra.addRule(new PlayIfCertian());
        pra.addRule(new DiscardIfCertian());
        return pra;
    }


    public static Agent buildRandom() {
        ProductionRuleAgent pra = new ProductionRuleAgent();
        pra.addRule(new LegalRandom());
        return pra;
    }

}
