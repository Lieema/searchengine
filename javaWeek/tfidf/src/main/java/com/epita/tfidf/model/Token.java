package com.epita.tfidf.model;

import com.epita.utils.annotation.NotNull;
import com.epita.utils.annotation.Nullable;
import com.epita.utils.annotation.Pure;

import java.util.ArrayList;
import java.util.List;

public class Token {

    @NotNull public String word;
    public double frequency;
    @NotNull public List<Integer> positions;

    public Token(String word) {
        this.word = word;
        positions = new ArrayList<>();
    }

    @Pure
    @Override
    public String toString() {
        return "Token{" +
                "word='" + word + '\'' +
                '}';
    }
}
