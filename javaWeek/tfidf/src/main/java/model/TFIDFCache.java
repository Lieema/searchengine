package model;

import java.util.HashMap;

public class TFIDFCache {
    HashMap<String, Double> idfHashMap;

    public TFIDFCache() {
        idfHashMap = new HashMap<>();
    }

    public void putInCache(String token, Double idf) {
        idfHashMap.put(token, idf);
    }

    public Double getIdf(String token) {
        return idfHashMap.get(token);
    }

}
