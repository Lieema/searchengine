package com.epita.domain.websocket.retroIndex;

import com.epita.domain.command.RetroIndexDocumentCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Document;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class RetroIndexDocumentCommandWebsocket extends WebSocketClient {

    public RetroIndexDocumentCommandWebsocket(URI serverUri) {
        super(serverUri);
    }

    public void SendCommand(RetroIndexDocumentCommand command) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(command);
            send(json);
        } catch (JsonProcessingException e) {
        }
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
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
