package service.cleanup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;

public abstract class Cleaner {
    public String clean(final String html) {
        String safeText = Jsoup.clean(html, Whitelist.none());

        return Parser.unescapeEntities(safeText, false);
    };
}
