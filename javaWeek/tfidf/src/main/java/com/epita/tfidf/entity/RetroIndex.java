package com.epita.tfidf.entity;

import com.epita.utils.annotation.Mutate;
import com.epita.utils.annotation.NotNull;
import com.epita.utils.annotation.Pure;
import com.epita.tfidf.model.Document;
import com.epita.tfidf.model.Token;
import com.epita.tfidf.service.dependencies.compute.MathIdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RetroIndex {

    @NotNull
    private HashMap<String, List<Document>> indexHashMap = new HashMap<>();

    @NotNull
    private List<Document> documents = new ArrayList<>();

    @NotNull
    private TFIDFCache idfCache = new TFIDFCache();

    @Pure
    public @NotNull Integer getDocumentNumber() {
        return documents.size();
    }

    @Pure
    public @NotNull List<Document> getDocuments(@NotNull final String token) {
        if (!contains(token))
            return new ArrayList<>();

        return indexHashMap.get(token);
    }

    @Pure
    public boolean contains(@NotNull final String token) {
        return indexHashMap.containsKey(token);
    }

    @Mutate
    public void addDocument(@NotNull final Document doc) {

        if (documents.contains(doc))
            return;
        documents.add(doc);

        doc.tokens.stream().forEach(token -> {
            if (indexHashMap.containsKey(token.word))
                indexHashMap.get(token.word).add(doc);
            else {
                List<Document> arr = new ArrayList<>();
                arr.add(doc);
                indexHashMap.put(token.word, arr);
            }

            idfCache.putInCache(token.word,
                    MathIdf.computeIDF((double)documents.size(), (double) indexHashMap.get(token.word).size()));
        });
    }

    @Pure
    public @NotNull Double getTokenIdf(@NotNull final String token) {
        return idfCache.getIdf(token);
    }

    @Pure
    public @NotNull List<Double> createDocumentTfIdfVector(@NotNull final Document doc,
                                                           @NotNull final List<Token> tokens) {
        List<Double> vector = new ArrayList<>();

        Map<String, Double> tokenFrequency = new HashMap<>();
        doc.tokens.stream().forEach(token -> tokenFrequency.put(token.word, token.frequency));

        tokens.stream().forEach(token -> {
            vector.add(tokenFrequency.containsKey(token.word) ?
                    getTokenIdf(token.word) * tokenFrequency.get(token.word) : 0.d);
        });

        return MathIdf.normalize(vector);
    }

}
