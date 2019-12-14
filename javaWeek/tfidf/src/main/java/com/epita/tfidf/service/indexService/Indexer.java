package com.epita.tfidf.service.indexService;

import com.epita.utils.annotation.NotNull;
import com.epita.utils.annotation.Pure;
import com.epita.tfidf.service.queryService.DefaultQueryService;
import com.epita.tfidf.entity.RetroIndex;
import com.epita.utils.logger.Logger;
import com.epita.tfidf.model.*;
import org.jsoup.Jsoup;
import com.epita.tfidf.service.dependencies.cleaner.Cleaner;
import com.epita.tfidf.service.dependencies.tokenizer.Tokenizer;
import com.epita.tfidf.service.dependencies.vectorizer.Vectorizer;

import java.io.IOException;
import java.util.List;

public abstract class Indexer extends Logger {

    @NotNull
    protected Cleaner cleaner;

    @NotNull
    protected Tokenizer tokeniser;

    @NotNull
    protected Vectorizer vectoriser;

    // BEGIN TMP
    DefaultQueryService defaultQueryService = new DefaultQueryService();
    RetroIndex retroIndex = new RetroIndex();
    // END TMP

    public Indexer() {
        defaultQueryService.setRetroIndex(retroIndex);
    }

    @Pure
    public Document getDocumentFromLink(@NotNull final String link) {
        logger.info("Enter getDocumentFromLink with" + link);
        Document res = new Document();

        try {
            res.html = Jsoup.connect(link).get().html();
        } catch (IOException e) {
            logger.error("Wrong url " + link);
            throw new RuntimeException("Wrong url");
        }

        res.link = link;
        logger.trace("Enter cleaner");
        res.text = cleaner.clean(res.html);

        logger.trace("Enter tokenizer");
        List<String> stringToks = tokeniser.getTokens(res.text);

        logger.trace("Enter vectorizer");
        List<Token> hashTokens = vectoriser.convert(stringToks);

        res.tokens = hashTokens;

        indexDocument(res);

        logger.info("Return correct document from " + link);
        return res;
    }

    // BEGIN TMP
    public List<Document> processQuery(String query) {
        return defaultQueryService.processQuery(query);
    }
    // END TMP

    @Pure
    public void indexDocument(final Document doc) {
        logger.info("Send message to index document");
        retroIndex.addDocument(doc);
    }
}
