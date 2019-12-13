package service;

import logger.Logger;
import model.*;
import service.cleanup.BaseCleaner;
import service.cleanup.Cleaner;
import org.jsoup.Jsoup;
import service.query.BaseQuery;
import service.token.BaseTokenisation;
import service.token.Tokenisation;
import service.vector.BaseVectorisation;
import service.vector.Vectorisation;

import java.io.IOException;
import java.util.List;

public class Index extends Logger {
    Cleaner cleaner = new BaseCleaner();
    Tokenisation tokeniser = new BaseTokenisation();
    Vectorisation vectoriser = new BaseVectorisation();
    BaseQuery baseQuery = new BaseQuery();


    RetroIndex retroIndex = new RetroIndex();

    public RetroIndex getRetroIndex() {
        return retroIndex;
    }

    public Document getDocumentFromLink(final String link) {
        Document res = new Document();

        res.setLink(link);
        try {
            res.setHtml(Jsoup.connect(link).get().html());
        } catch (IOException e) {
            e.printStackTrace();
        }

        res.setText(cleaner.clean(res.getHtml()));

        List<String> stringToks = tokeniser.getTokens(res.getText());

        List<Token> hashTokens = vectoriser.convert(stringToks);

        res.setTokens(hashTokens);

        indexDocument(res);

        return res;
    }

    private Query getQueryFromString(final String string) {
        Query res = new Query();

        String text = cleaner.clean(string);

        List<String> stringToks = tokeniser.getTokens(text);

        List<Token> hashTokens = vectoriser.convert(stringToks);

        res.setTokens(hashTokens);

        return res;
    }

    public List<Document> processQuery(String query) {
        return baseQuery.processQuery(getQueryFromString(query), retroIndex);
    }

    public void indexDocument(final Document doc) {
        retroIndex.addDocument(doc);
    }
}
