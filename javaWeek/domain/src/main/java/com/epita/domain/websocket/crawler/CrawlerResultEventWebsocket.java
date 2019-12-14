package com.epita.domain.websocket.crawler;

import com.epita.domain.command.CrawlUrlCommand;
import com.epita.domain.event.CrawlerResultEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Document;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.print.Doc;
import java.io.IOException;
import java.net.URI;

public class CrawlerResultEventWebsocket extends WebSocketClient {

    //private Crawler crawler;

    //public CrawlerResultEventWebsocket(URI serverUri, Crawler crawler) {
     public CrawlerResultEventWebsocket(URI serverUri) {
            super(serverUri);

            //this.crawler = crawler;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
    }

    @Override
    public void onMessage(String s) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            CrawlUrlCommand command = mapper.readValue(s, CrawlUrlCommand.class);
            //Document doc = crawler.crawl(command.url);
            Document doc = null;

            CrawlerResultEvent event = new CrawlerResultEvent(doc);
            String json = mapper.writeValueAsString(event);
            send(json);

        } catch (IOException e) {
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
    }

    @Override
    public void onError(Exception e) {
    }
}
