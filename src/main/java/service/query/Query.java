package service.query;

import model.Document;
import model.RetroIndex;
import model.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Query {
    public List<Document> processQuery(model.Query query, RetroIndex retroIndex) {
        //TODO
        return null;
    }

    public Double computeIDF(final Token token, RetroIndex retroIndex) {
        Double res = 0.0;
        //TODO
        return res;
    }
}
