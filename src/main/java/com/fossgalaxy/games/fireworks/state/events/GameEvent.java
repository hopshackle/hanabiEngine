package com.fossgalaxy.games.fireworks.state.events;

import com.fossgalaxy.games.fireworks.state.GameState;

import java.io.Serializable;

public abstract class GameEvent implements Serializable {
    private final MessageType id;

    public GameEvent(MessageType id) {
        this.id = id;
    }

    public abstract void apply(GameState state, int myPlayerID);

    @Deprecated
    public void apply(GameState state){
        apply(state, -1);
    }

    public MessageType getEvent() {
        return id;
    }

    public boolean isVisibleTo(int playerID) {
        return true;
    }

}
