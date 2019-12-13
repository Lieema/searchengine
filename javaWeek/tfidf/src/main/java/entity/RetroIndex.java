package entity;

import model.Document;
import model.Token;
import service.dependencies.compute.MathIdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RetroIndex {
    private HashMap<String, List<Document>> indexHashMap = new HashMap<>();
    private List<Document> documents = new ArrayList<>();

    private TFIDFCache idfCache = new TFIDFCache();

    public Integer getDocumentNumber() {
        return documents.size();
    }

    public List<Document> getDocuments(String token) {
        if (!contains(token))
            return new ArrayList<>();

        return indexHashMap.get(token);
    }

    public boolean contains(String token) {
        return indexHashMap.containsKey(token);
    }

    public void addDocument(Document doc) {

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

    public Double getTokenIdf(String token) {
        return idfCache.getIdf(token);
    }

    public List<Double> createDocumentTfIdfVector(Document doc, List<Token> tokens, RetroIndex retroindex) {
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
