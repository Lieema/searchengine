package com.epita.retroindexer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.epita.tfidf.entity.RetroIndex;
import io.javalin.Javalin;
import com.epita.tfidf.model.Document;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epita.tfidf.service.dependencies.cleaner.DefaultCleaner;
import com.epita.tfidf.service.dependencies.tokenizer.DefaultTokenizer;
import com.epita.tfidf.service.dependencies.vectorizer.DefaultVectorizer;
import com.epita.tfidf.service.queryService.Query;

import java.util.List;

public class RestApi extends Query {
    Logger logger = LogManager.getLogger(RestApi.class);
    Javalin app;

    public RestApi(int port) {
        cleaner = new DefaultCleaner();
        tokeniser = new DefaultTokenizer();
        vectoriser = new DefaultVectorizer();
        retroIndex = new RetroIndex();

        this.app = Javalin.create().start(port);
        logger.info("[REST] Create rest server");

        app.get("/search/:query", ctx -> {
            logger.info("[REST] Receive query request");
            logger.debug("[REST] query : " + ctx.pathParam("query"));
            String searchResult = searchRetroIndex(ctx.pathParam("query"));
            if (searchResult == null) {
                logger.info("[REST] Search failed : sending 500 error");
                ctx.res.setStatus(500);
            }
            else {
                logger.info("[REST] Send query results");
                ctx.result(searchResult);
            }
        });
    }

    private String searchRetroIndex(String query) {
        logger.info("[REST] Process query in retro index");
        List<Document> documents = processQuery(query);
        logger.info("[REST] Process done : " + documents.size() + " documents found");
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(documents);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize list of documents");
            return null;
        }
    }

    public void addDocument(Document document) {
        retroIndex.addDocument(document);
    }
}
