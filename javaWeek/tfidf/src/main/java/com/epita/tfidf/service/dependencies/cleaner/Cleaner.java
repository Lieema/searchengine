package com.epita.tfidf.service.dependencies.cleaner;

import com.epita.utils.annotation.Pure;
import com.epita.utils.logger.Logger;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;

public abstract class Cleaner extends Logger {

    @Pure
    public String clean(final String html) {
        String safeText = Jsoup.clean(html, Whitelist.none());
        return Parser.unescapeEntities(safeText, false);
    };
}
