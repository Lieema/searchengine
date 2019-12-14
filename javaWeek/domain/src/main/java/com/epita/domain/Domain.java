package com.epita.domain;

import annotation.Mutate;
import annotation.NotNull;
import annotation.Pure;
import com.epita.domain.event.CrawlerConnectionEvent;
import com.epita.domain.event.CrawlerResultEvent;
import com.epita.domain.event.IndexerConnectionEvent;
import com.epita.domain.event.IndexerResultEvent;
import com.epita.domain.websocket.crawler.CrawlUrlCommandWebsocket;
import com.epita.domain.websocket.crawler.DomainCrawlerConnectionEventWebsocket;
import com.epita.domain.websocket.indexer.DomainIndexerConnectionEventWebsocket;
import com.epita.domain.websocket.indexer.IndexDocumentCommandWebsocket;
import com.epita.domain.websocket.retroIndex.RetroIndexDocumentCommandWebsocket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class Domain {

    private final String crawlerWS = "ws://subscribe/broadcast/crawler";
    private final String indexerWS = "ws://subscribe/broadcast/indexer";
    private final String retroIndexWS = "ws://subscribe/broadcast/retroIndex";

    private final Map<String, CrawlUrlCommandWebsocket> crawlerCommandWS = new HashMap<>();
    private final Map<String, IndexDocumentCommandWebsocket> indexerCommandWS = new HashMap<>();

    @NotNull private DomainCrawlerConnectionEventWebsocket crawlerConnectionWS;
    @NotNull private DomainIndexerConnectionEventWebsocket indexerConnectionWS;
    @NotNull private RetroIndexDocumentCommandWebsocket retroIndexCommandWS;


    public Domain() {

        try {
            crawlerConnectionWS = new DomainCrawlerConnectionEventWebsocket(new URI(crawlerWS), this);
            indexerConnectionWS = new DomainIndexerConnectionEventWebsocket(new URI(indexerWS), this);
            retroIndexCommandWS = new RetroIndexDocumentCommandWebsocket(new URI(retroIndexWS));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Mutate
    public void addCrawler(@NotNull final CrawlerConnectionEvent event){
        if (!crawlerCommandWS.containsKey(event.id)) {

            StringBuilder sb = new StringBuilder(crawlerWS);
            sb.append("/");
            sb.append(event.id);

            try {
                CrawlUrlCommandWebsocket ws = new CrawlUrlCommandWebsocket(new URI(sb.toString()), this);
                crawlerCommandWS.put(event.id, ws);

            } catch (URISyntaxException e) {
            }
        }
    }

    @Mutate
    public void addIndexer(@NotNull final IndexerConnectionEvent event) {
        if (!indexerCommandWS.containsKey(event.id)) {

            StringBuilder sb = new StringBuilder(indexerWS);
            sb.append("/");
            sb.append(event.id);

            try {
                IndexDocumentCommandWebsocket ws = new IndexDocumentCommandWebsocket(new URI(sb.toString()), this);
                indexerCommandWS.put(event.id, ws);

            } catch (URISyntaxException e) {
            }
        }
    }

    @Pure
    public void crawlUrl(@NotNull final String url) {
    }

    @Pure
    public void indexDocument(@NotNull final CrawlerResultEvent event) {
    }

    @Pure
    public void sendToRetroIndex(@NotNull final IndexerResultEvent event) {
    }
}
