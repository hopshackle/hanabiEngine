package com.fossgalaxy.games.fireworks.state;

import java.util.*;

public class BasicState implements GameState {
    private static final int[] HAND_SIZE = {-1, -1, 5, 5, 4, 4};
    private static final int MAX_INFOMATION = 8;
    private static final int MAX_LIVES = 3;
    private static final int MAX_SCORE = CardColour.values().length * 5;

    private final int handSize;

    private final TimedHand[] hands;
    private final Deck deck;
    private final Map<CardColour, Integer> table;
    private final List<Card> discard;

    private int information;
    private int lives;
    private int movesLeft;

    public BasicState(BasicState state) {
        this.handSize = state.handSize;
        this.deck = new Deck(state.deck);
        this.discard = new ArrayList<>(state.discard);
        this.information = state.information;
        this.lives = state.lives;
        this.movesLeft = state.movesLeft;

        this.table = new EnumMap<>(state.table);

        this.hands = new TimedHand[state.hands.length];
        for (int i = 0; i < hands.length; i++) {
            hands[i] = new TimedHand(state.hands[i]);
        }
    }

    public BasicState(int playerCount) {
        this(HAND_SIZE[playerCount], playerCount);
    }

    public BasicState(int handSize, int playerCount) {
        this.handSize = handSize;
        this.hands = new TimedHand[playerCount];
        this.deck = new Deck();
        this.table = new EnumMap<>(CardColour.class);
        this.discard = new ArrayList<>();
        this.movesLeft = playerCount;

        this.information = MAX_INFOMATION;
        this.lives = MAX_LIVES;

        for (int i = 0; i < playerCount; i++) {
            hands[i] = new TimedHand(handSize);
            hands[i].init();
        }
    }

    @Override
    public void init() {
        init(null);
    }

    @Override
    public void init(Long seed) {
        deck.init();
        if (seed != null) {
            deck.shuffle(seed);
        } else {
            deck.shuffle();
        }
        dealHands();
    }

    private void dealHands() {
        for (int hand = 0; hand < hands.length; hand++) {
            deal(hand);
        }
    }

    @Override
    public GameState getCopy() {
        return new BasicState(this);
    }

    /**
     * Get a version of the hand for a given player, with information about their hand removed.
     * <p>
     * The server side should <b>never</b> need to use this - as only updates are sent to each client they should not
     * see any information they are not permitted to see.
     * <p>
     * It's mostly good for simulation-based approaches.
     *
     * @param playerID the playerID of the hand you want to access
     * @return the hidden information version of the hand
     */
    public Hand getPerspective(int playerID) {
        return new ShieldedHand(hands[playerID]);
    }

    @Override
    public void addToDiscard(Card card) {
        discard.add(card);
    }

    @Override
    public Card drawFromDeck() {
        return deck.getTopCard();
    }

    @Override
    public Card getCardAt(int player, int slot) {
        assert player >= 0 : "playerID must be bigger than -1";
        assert player < hands.length : "player ID higher than number of players";

        return hands[player].getCard(slot);
    }

    @Override
    public Deck getDeck() {
        return deck;
    }

    @Override
    public Collection<Card> getDiscards() {
        return Collections.unmodifiableList(discard);
    }

    @Override
    public Hand getHand(int player) {
        assert player >= 0 : "playerID must be bigger than -1";
        assert player < hands.length : "player ID higher than number of players";

        return hands[player];
    }

    @Override
    public int getHandSize() {
        return handSize;
    }

    @Override
    public int getInfomation() {
        return information;
    }

    @Override
    public int getLives() {
        return lives;
    }

    @Override
    public int getPlayerCount() {
        return hands.length;
    }

    @Override
    public int getScore() {
        int total = 0;

        for (Integer val : table.values()) {
            total += val;
        }
        return total;
    }

    @Override
    public int getStartingInfomation() {
        return MAX_INFOMATION;
    }

    @Override
    public int getStartingLives() {
        return MAX_LIVES;
    }

    @Override
    public int getTableValue(CardColour colour) {
        assert colour != null : "colour should never be null";
        Integer curr = table.get(colour);
        return curr == null ? 0 : curr;
    }

    @Override
    public boolean isGameOver() {
        //check if we have run out of lives
        if (lives <= 0) {
            return true;
        }

        //check that the deck is empty, and all players have had last go
        if (!deck.hasCardsLeft() && movesLeft == 0) {
            return true;
        }

        //if we have a maximum score, we win!
        if (getScore() == MAX_SCORE) {
            return true;
        }

        return false;
    }

    @Override
    public void setCardAt(int player, int slot, Card card) {
        BasicHand hand = hands[player];
        hand.clear(slot);
        hand.setCard(slot, card);
    }

    @Override
    public void setInformation(int newValue) {
        assert newValue <= MAX_INFOMATION;
        assert newValue >= 0;
        information = newValue;
    }

    @Override
    public void setKnownValue(int player, int slot, Integer value, CardColour colour) {
        assert player >= 0 : "playerID must be bigger than -1";
        assert player < hands.length : "player ID higher than number of players";

        Hand hand = hands[player];
        hand.setCard(slot, new Card(value, colour));
    }

    @Override
    public void setLives(int newValue) {
        assert newValue <= MAX_LIVES;
        assert newValue >= 0;
        lives = newValue;
    }

    @Override
    public void setTableValue(CardColour colour, int value) {
        table.put(colour, value);
    }

    @Override
    public void tick() {
        if (!deck.hasCardsLeft()) {
            movesLeft--;
        }
    }

    @Override
    public void deal(int playerID) {
        for (int slot = 0; slot < handSize; slot++) {
            hands[playerID].setCard(slot, deck.getTopCard());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicState that = (BasicState) o;

        if (handSize != that.handSize) return false;
        if (information != that.information) return false;
        if (lives != that.lives) return false;
        if (movesLeft != that.movesLeft) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(hands, that.hands)) return false;
        if (!deck.equals(that.deck)) return false;
        if (!table.equals(that.table)) return false;
        return discard.equals(that.discard);

    }

    @Override
    public int hashCode() {
        int result = handSize;
        result = 31 * result + Arrays.hashCode(hands);
        result = 31 * result + deck.hashCode();
        result = 31 * result + table.hashCode();
        result = 31 * result + discard.hashCode();
        result = 31 * result + information;
        result = 31 * result + lives;
        result = 31 * result + movesLeft;
        return result;
    }
}
