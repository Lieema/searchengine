package com.epita.domain.websocket.indexer;

import com.epita.domain.Domain;
import com.epita.domain.command.IndexDocumentCommand;
import com.epita.domain.event.IndexerResultEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;

public class IndexDocumentCommandWebsocket extends WebSocketClient {

    private Domain domain;

    public IndexDocumentCommandWebsocket(URI serverUri, Domain domain) {
        super(serverUri);
        this.domain = domain;
    }

    public void SendCommand(IndexDocumentCommand command) {

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
            IndexerResultEvent event = mapper.readValue(s, IndexerResultEvent.class);
            domain.sendToRetroIndex(event);
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
