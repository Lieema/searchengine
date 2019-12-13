package model;

import service.compute.MathIdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        doc.getTokens().stream().forEach(token -> {
            if (indexHashMap.containsKey(token.getWord()))
                indexHashMap.get(token.getWord()).add(doc);
            else {
                List<Document> arr = new ArrayList<>();
                arr.add(doc);
                indexHashMap.put(token.getWord(), arr);
            }

            idfCache.putInCache(token.getWord(),
                    MathIdf.computeIDF((double)documents.size(), (double) indexHashMap.get(token.getWord()).size()));
        });
    }

    public Double getTokenIdf(String token) {
        return idfCache.getIdf(token);
    }
}
