package com.epita.domain.websocket.indexer;

import com.epita.domain.event.IndexerConnectionEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class IndexerConnectionEventWebsocket extends WebSocketClient {

    private Integer id;
    private URI serverUri;

    public IndexerConnectionEventWebsocket(URI serverUri, Integer id) {
        super(serverUri);
        this.id = id;
        this.serverUri = serverUri;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        IndexerConnectionEvent event = new IndexerConnectionEvent(id, serverUri.getHost());
        ObjectMapper mapper = new ObjectMapper();

        try {
            String json = mapper.writeValueAsString(event);
            send(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(String s) {
    }

    @Override
    public void onClose(int i, String s, boolean b) {
    }

    @Override
    public void onError(Exception e) {
    }
}
