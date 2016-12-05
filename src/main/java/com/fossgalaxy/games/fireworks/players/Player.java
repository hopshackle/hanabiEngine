package com.fossgalaxy.games.fireworks.players;

import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;

public interface Player {

    String getName();

    Action getAction();

    void sendMessage(GameEvent msg);

    void setID(int id, int nPlayers);

}