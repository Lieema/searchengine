package com.epita.crawler.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Crawler {
    private final Logger logger = LogManager.getLogger(Crawler.class);

    @NotNull private String url;
    private List<String> foundUrls;

    public List<String> getFoundUrls() {
        return foundUrls;
    }

    public Crawler(@NotNull final String url) {
        this.url = url;
        this.foundUrls = new ArrayList<>();
    }


    public void crawl() {
        try {
            logger.info("[CRAWLER] Get document from : " + url);
            final Document doc = Jsoup.connect(url).get();
            logger.info("[CRAWLER] Get links from : " + url);
            final Elements links = doc.select("a[href]");
            logger.debug("[CRAWLER] Found " + foundUrls.size() + " links");

            int count = 0;
            for (Element link : links) {
                if (count > 100)
                    return;
                final String linkUrl = link.attr("abs:href");
                logger.trace("[CRAWLER] Add link : " + linkUrl);
                if (!linkUrl.contains("#") && !linkUrl.contains(".jpg") && !linkUrl.contains(".svg") && !foundUrls.contains(linkUrl)) {
                    foundUrls.add(linkUrl);
                    count++;
                }
            }
        } catch (IOException e) {
            logger.error("[CRAWLER] Fail to load html document");
        }
    }
}
