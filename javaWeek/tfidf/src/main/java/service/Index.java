package service;

import model.*;
import service.cleanup.BaseCleaner;
import service.cleanup.Cleaner;
import org.jsoup.Jsoup;
import service.compute.ComputeIDF;
import service.query.BaseQuery;
import service.token.BaseTokenisation;
import service.token.Tokenisation;
import service.vector.BaseVectorisation;
import service.vector.Vectorisation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Index {
    Cleaner cleaner = new BaseCleaner();
    Tokenisation tokeniser = new BaseTokenisation();
    Vectorisation vectoriser = new BaseVectorisation();
    BaseQuery baseQuery = new BaseQuery();
    RetroIndex retroIndex = new RetroIndex();
    TFIDFCache idfCache = new TFIDFCache();

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
        return baseQuery.processQuery(getQueryFromString(query), retroIndex, idfCache);
    }

    public void indexDocument(final Document doc) {

        for (Token token: doc.getTokens()) {
            if (retroIndex.getIndexHashMap().containsKey(token.toString())) {
                retroIndex.getIndexHashMap().get(token.toString()).add(doc);
            }
            else {
                retroIndex.getIndexHashMap().put(token.toString(), new ArrayList<Document>() { { add(doc); } });
            }
        }
        retroIndex.countDocument();

        for (Token token: doc.getTokens()) {
            idfCache.putInCache(token.getWord(), ComputeIDF.computeIDF((double)retroIndex.getDocumentNumber(), (double) retroIndex.getIndexHashMap().get(token.getWord()).size()));
        }
    }
}
