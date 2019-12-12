package model;

import java.util.HashMap;

public class TFIDFCache {
    HashMap<String, Double> tfidfHashMap;

    public TFIDFCache() {
        tfidfHashMap = new HashMap<>();
    }

    public HashMap<String, Double> getTfidfHashMap() {
        return tfidfHashMap;
    }
}
