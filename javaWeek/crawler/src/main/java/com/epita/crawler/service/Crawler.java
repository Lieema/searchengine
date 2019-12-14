package com.epita.crawler.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Crawler {
    private Logger logger = LogManager.getLogger(Crawler.class);

    private String url;
    private List<String> foundUrls;

    public List<String> getFoundUrls() {
        return foundUrls;
    }

    public Crawler(String url) {
        this.url = url;
        this.foundUrls = new ArrayList<String>();
    }

    public void crawl() {
        try {
            logger.info("[CRAWLER] Get document from : " + url);
            Document doc = Jsoup.connect(url).get();
            logger.info("[CRAWLER] Get links from : " + url);
            Elements links = doc.select("a[href]");
            logger.debug("[CRAWLER] Found " + foundUrls.size() + " links");

            for (Element link : links) {
                String linkUrl = link.attr("abs:href");
                logger.trace("[CRAWLER] Add link : " + linkUrl);
                foundUrls.add(linkUrl);
            }
        } catch (IOException e) {
            logger.error("[CRAWLER] Fail to load html document");
        }
    }
}
