package com.epita.domain.websocket.crawler;

import com.epita.domain.Domain;
import com.epita.domain.event.CrawlerConnectionEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;

public class DomainCrawlerConnectionEventWebsocket extends WebSocketClient {

    private Domain domain;

    public DomainCrawlerConnectionEventWebsocket(URI serverUri, Domain domain) {
        super(serverUri);
        this.domain = domain;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
    }

    @Override
    public void onMessage(String s) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CrawlerConnectionEvent event = objectMapper.readValue(s, CrawlerConnectionEvent.class);
            domain.addCrawler(event);
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
