package service.queryService;

import annotation.Mutate;
import annotation.NotNull;
import annotation.Pure;
import logger.Logger;
import model.Document;
import entity.RetroIndex;
import model.Token;
import service.dependencies.cleaner.Cleaner;
import service.dependencies.compute.MathIdf;
import service.dependencies.tokenizer.Tokenizer;
import service.dependencies.vectorizer.Vectorizer;
import util.Pair;

import java.util.*;

public abstract class Query extends Logger {

    @NotNull
    protected Cleaner cleaner;

    @NotNull
    protected Tokenizer tokeniser;

    @NotNull
    protected Vectorizer vectoriser;

    @NotNull
    protected RetroIndex retroIndex;

    @Mutate
    public void setRetroIndex(@NotNull final RetroIndex retroIndex) {
        this.retroIndex = retroIndex;
    }

    @Pure
    public List<Document> processQuery(@NotNull final String request) {
        logger.info("Enter processQuery: " + request);
        final model.Query query = new model.Query();

        logger.trace("Enter cleaner");
        final String text = cleaner.clean(request);

        logger.trace("Enter tokenizer");
        final List<String> stringToks = tokeniser.getTokens(text);

        logger.trace("Enter vectorizer");
        final List<Token> hashTokens = vectoriser.convert(stringToks);

        query.tokens = hashTokens;

        return processQuery(query);
    }

    @Pure
    protected List<Document> processQuery(@NotNull final model.Query query) {
        //tf.idf vectorizer of queryService
        List<Double> v1 = new ArrayList<>();

        // Set of documents which contains at least a tokenizer
        Set<Document> checkedDocument = new HashSet<>();

        //tf.idf vectors for each documents
        HashMap<Document, List<Double>> v2 = new HashMap<>();

        logger.info("Loop on query's tokens");
        for (Token token: query.tokens) {
            //check if tokenizer exists in retroindex
            if (!retroIndex.contains(token.word))
                continue;

            logger.trace("Token " + token + " exists in retroIndex");
            List<Document> docs = retroIndex.getDocuments(token.word);
            logger.trace(docs.size() + " documents contains token " + token);

            //compute idf for current queryService tokenizer
            Double idf = retroIndex.getTokenIdf(token.word);
            logger.trace("Token " + token + ", idf " + idf);

            //add tf.idf to first vectorizer
            v1.add(token.frequency * idf);

            checkedDocument.addAll(docs);
        }
        logger.info(checkedDocument.size() + " document(s) are concerned by query");

        logger.trace("Normalize documents tfidf vector");
        checkedDocument.stream().forEach(doc -> v2.put(doc, retroIndex.createDocumentTfIdfVector(doc, query.tokens)));

        logger.trace("Normalize query tfidf vector");
        MathIdf.normalize(v1);

        logger.info("Compute documents' similarity with query");
        List<Pair<Double, Document>> similarities = new ArrayList<>();
        for (Document doc : v2.keySet()) {
            Double coef = MathIdf.dotProd(v1, v2.get(doc)) / MathIdf.distance(v1, v2.get(doc));
            similarities.add(new Pair<>(coef, doc));
        }

        //sort the documents by descending
        logger.trace("Sort document, descending order");
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
        logger.info("Process query success");
        return sortedStats;
    }
}
