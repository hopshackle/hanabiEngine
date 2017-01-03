package com.fossgalaxy.games.fireworks.state;


import java.util.Arrays;

/**
 * Created by webpigeon on 18/10/16.
 */
public class TimedHand extends NegativeHand {
    private int[] time;
    private int drawTime;

    public TimedHand(TimedHand hand) {
        super(hand);
        this.drawTime = hand.drawTime;
        this.time = Arrays.copyOf(hand.time, hand.getSize());
    }

    public TimedHand(int size) {
        super(size);
        this.drawTime = 0;
        this.time = new int[size];
    }

    @Override
    public void setHasCard(int slot, boolean value) {
        super.setHasCard(slot, value);
        time[slot] = drawTime;
        drawTime++;
    }

    public int getOldestSlot() {
        int oldest = -1;
        int oldestTime = Integer.MAX_VALUE;

        for (int i = 0; i < time.length; i++) {
            if (hasCard(i) && time[i] < oldestTime) {
                oldest = i;
                oldestTime = time[i];
            }
        }

        assert oldestTime != -1 : "oldest slot failed";
        return oldest;
    }

    public int getNewestSlot() {
        int newest = -1;
        int newestTime = -1;

        for (int i = 0; i < time.length; i++) {
            if (hasCard(i) && time[i] > newestTime) {
                newest = i;
                newestTime = time[i];
            }
        }

        return newest;
    }

    public int getAge(int i) {
        return time[i];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TimedHand cards = (TimedHand) o;

        if (drawTime != cards.drawTime) return false;
        return Arrays.equals(time, cards.time);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(time);
        result = 31 * result + drawTime;
        return result;
    }
}
