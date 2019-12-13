package model;

import java.util.ArrayList;
import java.util.List;

public class Token {

    public String word;
    public double frequency;
    public List<Integer> positions;

    public Token(String word) {
        this.word = word;
        positions = new ArrayList<>();
    }
}
