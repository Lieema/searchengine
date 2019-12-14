package com.epita.domain;

import annotation.Mutate;
import annotation.NotNull;
import annotation.Pure;
import com.epita.domain.websocket.command.CrawlUrlCommandWS;
import com.epita.domain.websocket.command.IndexDocumentCommandWS;
import com.epita.domain.websocket.connection.DomainCrawlerConnectionWS;
import com.epita.domain.websocket.connection.DomainIndexerConnectionWS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Message;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class Domain {

    private final String crawlerConnection = "ws://localhost:8080/subscribe/broadcast/crawler_connection_event";
    private final String crawlerCommand = "ws://localhost:8080/subscribe/broadcast/crawl_url_command/";
    private final String crawlerResult = "ws://localhost:8080/subscribe/broadcast/crawler_result_event/";
    private final String indexerConnection = "ws://localhost:8080/subscribe/broadcast/indexer_connection_event";
    private final String indexerCommand = "ws://localhost:8080/subscribe/broadcast/index_document_command/";
    private final String indexerResult = "ws://localhost:8080/subscribe/broadcast/indexer_result_event/";

    private final Map<String, CrawlUrlCommandWS> crawlerCommandWS = new HashMap<>();
    private final Queue<String> crawlerAvailable = new LinkedList<>();

    private final Map<String, IndexDocumentCommandWS> indexerCommandWS = new HashMap<>();
    private final Queue<String> indexerAvailable = new LinkedList<>();

    @NotNull private DomainCrawlerConnectionWS crawlerConnectionWS;
    @NotNull private DomainIndexerConnectionWS indexerConnectionWS;


    private final Queue<String> urlToCrawl = new LinkedList<>();
    private final Queue<String> urlToIndex = new LinkedList<>();

    private boolean isRunning = true;

    public Domain() {
        try {
            crawlerConnectionWS = new DomainCrawlerConnectionWS(new URI(crawlerConnection), this);
            indexerConnectionWS = new DomainIndexerConnectionWS(new URI(indexerConnection), this);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Mutate
    public void startEventLoop() {
        while (isRunning) {

            if (urlToCrawl.size() != 0 && crawlerAvailable.size() != 0) {
                String url = urlToCrawl.poll();
                crawlUrl(url);
            }

            if (urlToIndex.size() != 0 && indexerAvailable.size() != 0) {
                String url = urlToIndex.poll();
                indexDocument(url);
            }
        }
    }

    @Mutate
    public void addCrawler(@NotNull final String id){
        if (!crawlerCommandWS.containsKey(id)) {

            StringBuilder commandUrl = new StringBuilder(crawlerCommand);
            commandUrl.append(id);

            StringBuilder resultUrl = new StringBuilder(crawlerResult);
            resultUrl.append(id);

            try {
                CrawlUrlCommandWS ws = new CrawlUrlCommandWS(
                        new URI(commandUrl.toString()), new URI(resultUrl.toString()), this);
                crawlerCommandWS.put(id, ws);
                crawlerAvailable.add(ws);
                ws.startEventLoop();

            } catch (URISyntaxException e) {
            }
        }
    }

    @Mutate
    public void addIndexer(@NotNull final String id) {
        if (!indexerCommandWS.containsKey(id)) {

            StringBuilder commandUrl = new StringBuilder(indexerCommand);
            commandUrl.append(id);

            StringBuilder resultUrl = new StringBuilder(indexerResult);
            resultUrl.append(id);

            try {
                IndexDocumentCommandWS ws = new IndexDocumentCommandWS(
                        new URI(commandUrl.toString()), new URI(resultUrl.toString()),this);
                indexerCommandWS.put(id, ws);
                indexerAvailable.add(ws);
                ws.startEventLoop();

            } catch (URISyntaxException e) {
            }
        }
    }

    @Pure
    public void crawlUrl(@NotNull final String url) {

        try {
            String id = crawlerAvailable.poll();
            CrawlUrlCommandWS ws = crawlerCommandWS.get(id);
            String json = new ObjectMapper().writeValueAsString(url);
            Message m = new Message(json, String.class.getName(), id);
            ws.sendMessage(m);

        } catch (JsonProcessingException e) {
        }
    }

    @Pure
    public void indexDocument(@NotNull final String url) {
        try {
            String id = indexerAvailable.poll();
            IndexDocumentCommandWS ws = indexerCommandWS.get(id);
            String json = new ObjectMapper().writeValueAsString(url);
            Message m = new Message(json, String.class.getName(), id);
            ws.sendMessage(m);

        } catch (JsonProcessingException e) {
        }
    }


    @Mutate
    public void updateCrawlerQueue(@NotNull final String id) {

        CrawlUrlCommandWS ws = crawlerCommandWS.get(id);
        crawlerAvailable.add(id);

    }

    @Mutate
    public void updateIndexerQueue(@NotNull final String id) {

        IndexDocumentCommandWS ws = indexerCommandWS.get(id);
        indexerAvailable.add(id);
    }

    @Mutate
    public void addUrlToCrawn(@NotNull final String url) {
        urlToCrawl.add(url);
    }

    @Mutate
    public void addUrlToIndex(@NotNull final String url) {
        urlToIndex.add(url);
    }
}