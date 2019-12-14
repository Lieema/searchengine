package com.epita.tfidf.model;

import com.epita.utils.annotation.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Query {

    @NotNull public List<Token> tokens;

    public Query() {
        tokens = new ArrayList<>();
    }
}
