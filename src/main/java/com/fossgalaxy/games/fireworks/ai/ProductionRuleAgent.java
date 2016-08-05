package com.fossgalaxy.games.fireworks.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fossgalaxy.games.fireworks.ai.rule.DiscardIfUseless;
import com.fossgalaxy.games.fireworks.ai.rule.PlayIfCertian;
import com.fossgalaxy.games.fireworks.ai.rule.Rule;
import com.fossgalaxy.games.fireworks.ai.rule.TellAnyoneAboutUsefulCard;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.DiscardCard;
import com.fossgalaxy.games.fireworks.state.actions.PlayCard;

public class ProductionRuleAgent implements Agent {
	private List<Rule> rules;
	private Random random;
	
	public ProductionRuleAgent() {
		this.rules = new ArrayList<>();
		this.random = new Random();
		
		rules.add(new TellAnyoneAboutUsefulCard());
		rules.add(new PlayIfCertian());
		//rules.add(new TellAboutUsefulCard());
		rules.add(new DiscardIfUseless());
	}

	@Override
	public Action doMove(int agentID, GameState state) {
		
		for (Rule rule : rules) {
			if (rule.canFire(agentID, state)) {
				return rule.execute(agentID, state);
			}
		}
		
		return doDefaultBehaviour(agentID, state);
	}
	
	
	//default rule based behaviour, discard random if legal, else play random
	public Action doDefaultBehaviour(int playerID, GameState state) {
		if (state.getInfomation() != state.getStartingInfomation()) {
			return new DiscardCard(random.nextInt(state.getHandSize()));
		} else {
			return new PlayCard(random.nextInt(state.getHandSize()));
		}
	}
	
}
