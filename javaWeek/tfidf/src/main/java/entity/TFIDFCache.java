package entity;

import annotation.Mutate;
import annotation.NotNull;
import annotation.Pure;

import java.util.HashMap;

public class TFIDFCache {
    @NotNull
    HashMap<String, Double> idfHashMap;

    public TFIDFCache() {
        idfHashMap = new HashMap<>();
    }

    @Mutate
    public void putInCache(@NotNull final String token, @NotNull final Double idf) {
        idfHashMap.put(token, idf);
    }

    @Pure
    public @NotNull Double getIdf(@NotNull final String token) {
        return idfHashMap.get(token);
    }

}
