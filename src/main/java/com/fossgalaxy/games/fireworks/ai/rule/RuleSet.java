package com.fossgalaxy.games.fireworks.ai.rule;

import com.fossgalaxy.games.fireworks.ai.osawa.rules.OsawaDiscard;
import com.fossgalaxy.games.fireworks.ai.osawa.rules.TellPlayableCardOuter;
import com.fossgalaxy.games.fireworks.ai.rule.random.*;
import com.fossgalaxy.games.fireworks.ai.rule.simple.DiscardIfCertain;
import com.fossgalaxy.games.fireworks.ai.rule.simple.PlayIfCertain;

import java.util.ArrayList;

/**
 * Created by piers on 27/01/17.
 */
public class RuleSet {
    public static ArrayList<Rule> getRules() {
        ArrayList<Rule> rules = new ArrayList<>();
        // Discard rules
        rules.add(new DiscardRandomly());
        rules.add(new DiscardIfCertain());
        rules.add(new DiscardSafeCard());
        rules.add(new DiscardOldestFirst());
        rules.add(new DiscardUselessCard());
        rules.add(new OsawaDiscard());
        rules.add(new DiscardLeastLikelyToBeNecessary());
        rules.add(new DiscardProbablyUselessCard());

        // Tell rules
        rules.add(new TellPlayableCard());
        rules.add(new TellPlayableCardOuter());
        rules.add(new TellRandomly());
        rules.add(new TellAboutOnes());
        rules.add(new TellAnyoneAboutUsefulCard());
        rules.add(new TellUnknown());
        rules.add(new TellAnyoneAboutUselessCard());
        rules.add(new TellDispensable());
        rules.add(new TellMostInformation());


        // Play Rules
        rules.add(new PlaySafeCard());
        rules.add(new PlayIfCertain());
        rules.add(new PlayProbablySafeCard(.1));
        rules.add(new PlayProbablySafeCard(.2));
        rules.add(new PlayProbablySafeCard(.3));
        rules.add(new PlayProbablySafeCard(.4));
        rules.add(new PlayProbablySafeCard(.5));
        rules.add(new PlayProbablySafeCard(.6));
        rules.add(new PlayProbablySafeCard(.7));
        rules.add(new PlayProbablySafeCard(.8));
        rules.add(new PlayProbablySafeCard(.9));

        // new extra funtime rules
        rules.add(new TellIllInformed());
        rules.add(new TellFives());
        rules.add(new CompleteTellUsefulCard());
        rules.add(new DiscardRandomly());
        rules.add(new DiscardOldestNoInfoFirst());

        rules.add(new TryToUnBlock());
        rules.add(new TellMostInformation(true));

        return rules;
    }
}
