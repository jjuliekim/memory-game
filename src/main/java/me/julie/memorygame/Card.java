package me.julie.memorygame;

public class Card {
    private final int value;
    private boolean flipped;
    private boolean matched;

    public Card(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void flip() {
        flipped = !flipped;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }
}
