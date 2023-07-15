package me.julie.memorygame;

public class Card {
    private int value;
    private boolean flipped;

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

}
