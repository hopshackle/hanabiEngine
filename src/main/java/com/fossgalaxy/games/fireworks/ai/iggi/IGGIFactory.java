package com.fossgalaxy.games.fireworks.ai.iggi;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.rule.DiscardIfUseless;
import com.fossgalaxy.games.fireworks.ai.rule.PlayIfCertian;
import com.fossgalaxy.games.fireworks.ai.rule.ProductionRuleAgent;
import com.fossgalaxy.games.fireworks.ai.rule.TellAnyoneAboutUsefulCard;
import com.fossgalaxy.games.fireworks.ai.rule.TellPlayableCardOuter;
import com.fossgalaxy.games.fireworks.ai.rule.random.DiscardRandomly;
import com.fossgalaxy.games.fireworks.ai.rule.random.TellPlayableCard;

/**
 * Stratergies used/theorised about by IGGI students.
 */
public class IGGIFactory {

	/**
	 * Cautious but helpful bot. 
	 * 
	 * This policy will only play cards it is sure about, it will discard cards it knows are useless
	 * before considering discarding randomly if it has no other choice.
	 * 
	 * @return an agent implementing the appropriate strategy
	 */
	public static Agent buildCautious() {
		ProductionRuleAgent pra = new ProductionRuleAgent();
		pra.addRule(new TellAnyoneAboutUsefulCard());
		pra.addRule(new PlayIfCertian());
		pra.addRule(new DiscardIfUseless());
		pra.addRule(new DiscardRandomly());
		return pra;
	}
	
	public static Agent buildTest2() {
		ProductionRuleAgent pra = new ProductionRuleAgent();
		pra.addRule(new TellPlayableCardOuter());
		pra.addRule(new PlayIfCertian());
		pra.addRule(new DiscardIfUseless());
		return pra;
	}
	
	
	public static Agent buildRandom() {
		ProductionRuleAgent pra = new ProductionRuleAgent();
		pra.addRule(new LegalRandom());
		return pra;
	}
	
}
