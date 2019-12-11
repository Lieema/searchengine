package model;

import java.util.ArrayList;
import java.util.List;

public class Token {

    private String word;
    private double frequency;
    private List<Integer> positions;

    public Token(String word) {
        this.word = word;
        positions = new ArrayList<>();
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public String toString() {
        return word;
    }
}
