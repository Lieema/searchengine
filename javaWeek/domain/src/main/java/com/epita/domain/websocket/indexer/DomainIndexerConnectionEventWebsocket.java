package com.epita.domain.websocket.indexer;

import com.epita.domain.Domain;
import com.epita.domain.event.IndexerConnectionEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;

public class DomainIndexerConnectionEventWebsocket extends WebSocketClient {

    private Domain domain;

    public DomainIndexerConnectionEventWebsocket(URI serverUri, Domain domain) {
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
            IndexerConnectionEvent event = objectMapper.readValue(s, IndexerConnectionEvent.class);
            domain.addIndexer(event);
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
