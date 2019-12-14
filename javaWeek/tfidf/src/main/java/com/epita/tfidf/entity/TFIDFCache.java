package com.epita.tfidf.entity;

import com.epita.utils.annotation.Mutate;
import com.epita.utils.annotation.NotNull;
import com.epita.utils.annotation.Pure;

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
