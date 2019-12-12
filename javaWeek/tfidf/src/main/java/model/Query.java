package model;

import java.util.ArrayList;
import java.util.List;

public class Query {
    private List<Token> tokens;

    public Query() {
        tokens = new ArrayList<>();
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }
}
