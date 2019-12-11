package service;

import model.Query;
import service.cleanup.BaseCleaner;
import service.cleanup.Cleaner;
import model.Document;
import model.RetroIndex;
import model.Token;
import org.jsoup.Jsoup;
import service.token.BaseTokenisation;
import service.token.Tokenisation;
import service.vector.BaseVectorisation;
import service.vector.Vectorisation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Index {
    Cleaner cleaner = new BaseCleaner();
    Tokenisation tokeniser = new BaseTokenisation();
    Vectorisation vectoriser = new BaseVectorisation();
    RetroIndex retroIndex = new RetroIndex();

    public Document getDocumentFromLink(final String link) {
        Document res = new Document();

        res.setLink(link);
        try {
            res.setHtml(Jsoup.connect(link).get().html());
            res.setHtml("The blue rabbit is fishing in a blue river.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        res.setText(cleaner.clean(res.getHtml()));

        List<String> stringToks = tokeniser.getTokens(res.getText());

        List<Token> hashTokens = vectoriser.convert(stringToks);

        res.setTokens(hashTokens);

        return res;
    }

    public Query getQueryFromString(final String string) {
        Query res = new Query();

        String text = cleaner.clean(string);

        List<String> stringToks = tokeniser.getTokens(text);

        List<Token> hashTokens = vectoriser.convert(stringToks);

        res.setTokens(hashTokens);

        return res;
    }

    public void indexDocument(final Document doc) {
        for (Token token:
             doc.getTokens()) {
            if (retroIndex.getIndexHashMap().containsKey(token.toString())) {
                retroIndex.getIndexHashMap().get(token.toString()).add(doc);
            }
            else {
                retroIndex.getIndexHashMap().put(token.toString(), new ArrayList<Document>() { { add(doc); } });
            }
        }
        retroIndex.countDocument();
    }
}
