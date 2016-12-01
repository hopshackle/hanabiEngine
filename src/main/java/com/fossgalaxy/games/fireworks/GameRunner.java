package com.fossgalaxy.games.fireworks;

import com.fossgalaxy.games.fireworks.players.Player;
import com.fossgalaxy.games.fireworks.state.*;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.events.CardDrawn;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;
import com.fossgalaxy.games.fireworks.state.events.GameInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class GameRunner {
    private final Logger logger = LoggerFactory.getLogger(GameRunner.class);
    private static final int RULE_STRIKES = 3; //how many times can a player return an illegal move before we give up?
    private static final int[] HAND_SIZE = {-1, -1, 5, 5, 4, 4};

    private final String gameID;
    private final Player[] players;
    private final GameState state;


    private int nPlayers;
    private int moves;

    private int nextPlayer;

    public GameRunner(UUID id, int playersCount){
        this(id.toString(), playersCount);
    }

    public GameRunner(String gameID, int expectedPlayers) {
        assert expectedPlayers > 2 : "too few players";
        assert expectedPlayers < HAND_SIZE.length : "too many players";

        this.players = new Player[expectedPlayers];
        this.state = new BasicState(HAND_SIZE[expectedPlayers], expectedPlayers);
        this.nPlayers = 0;

        this.nextPlayer = 0;
        this.moves = 0;
        this.gameID = gameID;
    }

    public void addPlayer(Player player) {
        logger.info("player {} is {}", nPlayers, player);
        players[nPlayers++] = Objects.requireNonNull(player);
    }

    public void init(Long seed) {
        logger.info("game init started - {} player game with seed {}", players.length, seed);
        long startTime = getTick();

        //step 1: tell all players their IDs
        for (int i=0; i<players.length; i++) {
            logger.info("player {} is {}", i, players[i]);
            players[i].setID(i, players.length);
        }

        state.init(seed);

        //let the players know the game has started and the starting state
        send(new GameInformation(nPlayers, HAND_SIZE[nPlayers], state.getInfomation(), state.getLives()));

        //tell players about their hands
        for (int player = 0; player < players.length; player++) {
            Hand hand = state.getHand(player);

            for (int slot = 0; slot < hand.getSize(); slot++) {
                Card cardInSlot = hand.getCard(slot);
                send(new CardDrawn(player, slot, cardInSlot.colour, cardInSlot.value));
            }
        }

        long endTime = getTick();
        logger.info("Game init complete: took {} ms", endTime - startTime);
    }

    //TODO find a better way of doing this logging.
    private void writeState(GameState state) {
       /* if (gameOut == null) {
            return;
        }

        DebugUtils.printState(gameOut, state);*/
    }

    private long getTick() {
        return System.currentTimeMillis();
    }

    //TODO time limit the agent
    public void nextMove() {
        Player player = players[nextPlayer];
        assert player != null : "that player is not valid";

        logger.debug("asking player {} for their move", nextPlayer);
        long startTime = getTick();

        //get the action and try to apply it
        Action action = player.getAction();

        long endTime = getTick();
        logger.debug("agent {} took {} ms to make their move", nextPlayer, endTime-startTime);
        logger.debug("move {}: player {} made move {}", moves, nextPlayer, action);

        //if the more was illegal, throw a rules violation
        if (!action.isLegal(nextPlayer, state)) {
            throw new RulesViolation(action);
        }

        //perform the action and get the effects
        logger.info("player {} made move {} as turn {}", nextPlayer, action, moves);
        moves++;
        Collection<GameEvent> events = action.apply(nextPlayer, state);
        events.forEach(this::send);

        //make sure it's the next player's turn
        nextPlayer = (nextPlayer + 1) % players.length;
    }

    public GameStats playGame(Long seed) {
        assert nPlayers == players.length;
        init(seed);

        int strikes = 0;
        while (!state.isGameOver()) {
            try {
                writeState(state);
                nextMove();
                state.tick();
            } catch (RulesViolation rv) {
                logger.warn("got rules violation when processing move", rv);
                strikes++;

                //If we're not being permissive, end the game.
                if (strikes <= RULE_STRIKES) {
                    logger.error("Maximum strikes reached, ending game");
                    break;
                }
            }
        }

        return new GameStats(gameID, players.length, state.getScore(), state.getLives(), moves, state.getInfomation(), strikes);
    }

    //send messages as soon as they are available
    private void send(GameEvent event) {
        logger.debug("game sent event: {}", event);
        for (int i = 0; i < players.length; i++) {
            if (event.isVisibleTo(i)) {
                players[i].sendMessage(event);
            }
        }
    }

}
