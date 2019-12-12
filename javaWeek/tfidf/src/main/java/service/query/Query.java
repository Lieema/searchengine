package service.query;

import model.Document;
import model.RetroIndex;
import model.TFIDFCache;
import model.Token;
import service.compute.ComputeIDF;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingDouble;

public abstract class Query {
    public abstract ComputeIDF getComputeIDF();

    public List<Document> processQuery(model.Query query, RetroIndex retroIndex) {
        //tf.idf vector of query
        List<Double> v1 = new ArrayList<>();
        //tf.idf vectors for each documents
        HashMap<Document, List<Double>> v2 = new HashMap<>();

        for (Token token:
             query.getTokens()) {
            //check if token exists in retroindex
            List<Document> docs = retroIndex.getIndexHashMap().get(token.toString());
            if (docs == null) {
                docs = new ArrayList<>();
            }

            if (docs.size() == 0) {
                continue;
            }

            //compute idf for current query token
            Double actuIDF = getComputeIDF().computeIDF((double)retroIndex.getDocumentNumber(),
                    (double)docs.size());

            //add tf.idf to first vector
            v1.add(token.getFrequency() * actuIDF);

            //foreach document linked to the current token
            for (Document actuDoc :
                    docs) {
                //foreach token in this document
                for (Token docToken :
                        actuDoc.getTokens()) {
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
        }

        //normalize each vectors
        getComputeIDF().normalize(v1);
        for (List<Double> list:
             v2.values()) {
            getComputeIDF().normalize(list);
        }

        //create a hashmap to link a document to its 'proximity score'
        HashMap<Document, Double> intermediate = new HashMap<>();
        for (Document doc :
                v2.keySet()) {
            Double coef = getComputeIDF().dotProd(v1, v2.get(doc)) / getComputeIDF().distance(v1, v2.get(doc));
            intermediate.put(doc, coef);
        }

        //sort the documents by descending
        final List<Document> sortedStats = intermediate.entrySet().stream()
                .sorted(comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return sortedStats;
    }
}
