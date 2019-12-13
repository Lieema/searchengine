package service.query;

import model.Document;
import model.RetroIndex;
import model.TFIDFCache;
import model.Token;
import service.compute.ComputeIDF;
import util.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.*;
import static java.util.Comparator.comparingDouble;

public abstract class Query {
    public abstract ComputeIDF getComputeIDF();

    public List<Document> processQuery(model.Query query, RetroIndex retroIndex, TFIDFCache idfcache) {
        //tf.idf vector of query
        List<Double> v1 = new ArrayList<>();

        // Set of documents which contains at least a token
        Set<Document> checkedDocument = new HashSet<>();

        //tf.idf vectors for each documents
        HashMap<Document, List<Double>> v2 = new HashMap<>();

        for (Token token: query.getTokens()) {
            //check if token exists in retroindex
            if (!retroIndex.getIndexHashMap().containsKey(token.toString()))
                continue;

            List<Document> docs = retroIndex.getIndexHashMap().get(token.toString());

            //compute idf for current query token
            Double actuIDF = idfcache.getIdf(token.getWord());

            //add tf.idf to first vector
            v1.add(token.getFrequency() * actuIDF);

            checkedDocument.addAll(docs);
        }

        // Compute TfIdf normalized vector
        checkedDocument.stream().forEach(doc -> v2.put(doc, createDocumentTfIdfVector(doc, query.getTokens(), idfcache)));

        //normalize each vectors
        getComputeIDF().normalize(v1);

        //create a hashmap to link a document to its 'proximity score'
        List<Pair<Double, Document>> similarities = new ArrayList<>();
        for (Document doc : v2.keySet()) {
            Double coef = getComputeIDF().dotProd(v1, v2.get(doc)) / getComputeIDF().distance(v1, v2.get(doc));
            similarities.add(new Pair<>(coef, doc));
        }

        //sort the documents by descending
        similarities.sort(new Comparator<Pair<Double, Document>>() {
            @Override
            public int compare(Pair<Double, Document> o1, Pair<Double, Document> o2) {
                if (o1.first.equals(o2.first))
                    return 0;
                else if (o1.first > o2.first)
                    return -1;

                return 1;
            }
        });

        List<Document> sortedStats = new ArrayList<>();
        similarities.stream().forEach(pair -> sortedStats.add(pair.second));
        return sortedStats;
    }

    private List<Double> createDocumentTfIdfVector(Document doc, List<Token> tokens, TFIDFCache idfcache) {
        List<Double> vector = new ArrayList<>();

        Map<String, Double> tokenFrequency = new HashMap<>();
        doc.getTokens().stream().forEach(token -> tokenFrequency.put(token.getWord(), token.getFrequency()));

        tokens.stream().forEach(token -> {
            vector.add(tokenFrequency.containsKey(token.getWord()) ?
                    idfcache.getIdf(token.getWord()) * tokenFrequency.get(token.getWord()) : 0.d);
        });

        return getComputeIDF().normalize(vector);
    }
}
