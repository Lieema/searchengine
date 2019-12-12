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

    public List<Document> processQuery(model.Query query, RetroIndex retroIndex) {
        //tf.idf vector of query
        List<Double> v1 = new ArrayList<>();

        // Set of documents which contains at least a token
        Set<Document> checkedDocument = new HashSet<>();

        //tf.idf vectors for each documents
        HashMap<Document, List<Double>> v2 = new HashMap<>();

        HashMap<String, Double> tokenIdf = new HashMap<>();

        for (Token token: query.getTokens()) {
            //check if token exists in retroindex
            if (!retroIndex.getIndexHashMap().containsKey(token.toString()))
                continue;

            List<Document> docs = retroIndex.getIndexHashMap().get(token.toString());

            //compute idf for current query token
            Double actuIDF = getComputeIDF().computeIDF((double)retroIndex.getDocumentNumber(), (double)docs.size());
            tokenIdf.put(token.getWord(), actuIDF);

            //add tf.idf to first vector
            v1.add(token.getFrequency() * actuIDF);

            checkedDocument.addAll(docs);

            /*

            //foreach document linked to the current token
            for (Document actuDoc : docs) {
                //foreach token in this document
                for (Token docToken : actuDoc.getTokens()) {
                    //look if our token is this docToken
                    //if so add it to v2, otherwise do nothing
                    if (token.toString().equals(docToken.toString())) {
                        if (v2.get(actuDoc) != null) {
                            v2.get(actuDoc).add(docToken.getFrequency() * actuIDF);
                        }
                        else {
                            v2.put(actuDoc, new ArrayList<Double>() { { add(docToken.getFrequency() * actuIDF); } });
                        }
                    }
                }
            }
            */
        }

        // Compute TfIdf normalized vector
        checkedDocument.stream().forEach(doc -> v2.put(doc, createDocumentTfIdfVector(doc, query.getTokens(), tokenIdf)));

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

    private List<Double> createDocumentTfIdfVector(Document doc, List<Token> tokens, HashMap<String, Double> tokenIdf) {
        List<Double> vector = new ArrayList<>();

        Map<String, Double> tokenFrequency = new HashMap<>();
        doc.getTokens().stream().forEach(token -> tokenFrequency.put(token.getWord(), token.getFrequency()));

        tokens.stream().forEach(token -> {
            vector.add(tokenFrequency.containsKey(token.getWord()) ?
                    tokenIdf.get(token.getWord()) * tokenFrequency.get(token.getWord()) : 0.d);
        });

        return getComputeIDF().normalize(vector);
    }
}
