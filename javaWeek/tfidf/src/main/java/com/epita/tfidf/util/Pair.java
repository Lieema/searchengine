package com.epita.tfidf.util;

public class Pair<TYPE_A, TYPE_B> {

    public TYPE_A first;
    public TYPE_B second;

    public Pair(TYPE_A first, TYPE_B second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second.toString() +
                '}';
    }

}
