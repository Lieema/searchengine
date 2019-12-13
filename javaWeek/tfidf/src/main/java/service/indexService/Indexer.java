package service.indexService;

import entity.RetroIndex;
import logger.Logger;
import model.*;
import org.jsoup.Jsoup;
import service.dependencies.cleaner.Cleaner;
import service.dependencies.tokenizer.Tokenizer;
import service.dependencies.vectorizer.Vectorizer;
import service.queryService.DefaultQueryService;

import java.io.IOException;
import java.util.List;

public abstract class Indexer extends Logger {
    protected Cleaner cleaner;
    protected Tokenizer tokeniser;
    protected Vectorizer vectoriser;

    // BEGIN TMP
    DefaultQueryService defaultQueryService = new DefaultQueryService();
    RetroIndex retroIndex = new RetroIndex();
    // END TMP

    public Indexer() {
        defaultQueryService.setRetroIndex(retroIndex);
    }

    public Document getDocumentFromLink(final String link) {
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

    public void indexDocument(final Document doc) {
        logger.info("Send message to index document");
        retroIndex.addDocument(doc);
    }
}
