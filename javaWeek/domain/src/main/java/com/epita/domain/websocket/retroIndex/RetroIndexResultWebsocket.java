package com.epita.domain.websocket.retroIndex;

import com.epita.domain.command.RetroIndexDocumentCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.RetroIndex;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;

public class RetroIndexResultWebsocket extends WebSocketClient {

    private RetroIndex retroIndex;

    public RetroIndexResultWebsocket(URI serverUri, RetroIndex retroIndex) {
        super(serverUri);
        this.retroIndex = retroIndex;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
    }

    @Override
    public void onMessage(String s) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            RetroIndexDocumentCommand command = mapper.readValue(s, RetroIndexDocumentCommand.class);
            retroIndex.addDocument(command.document);
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
