package com.epita.domain.websocket.crawler;

import com.epita.domain.Domain;
import com.epita.domain.command.CrawlUrlCommand;
import com.epita.domain.event.CrawlerResultEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;

public class CrawlUrlCommandWebsocket extends WebSocketClient {

    private Domain domain;

    public CrawlUrlCommandWebsocket(URI serverUri, Domain domain) {
        super(serverUri);
        this.domain = domain;
    }

    public void SendCommand(CrawlUrlCommand command) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(command);
            send(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
    }

    @Override
    public void onMessage(String s) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            CrawlerResultEvent event = mapper.readValue(s, CrawlerResultEvent.class);
            domain.indexDocument(event);

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
