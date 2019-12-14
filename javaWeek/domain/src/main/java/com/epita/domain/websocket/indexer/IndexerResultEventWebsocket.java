package com.epita.domain.websocket.indexer;

import com.epita.domain.command.IndexDocumentCommand;
import com.epita.domain.event.IndexerResultEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Document;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import service.indexService.Indexer;

import java.io.IOException;
import java.net.URI;

public class IndexerResultEventWebsocket extends WebSocketClient {

    private Indexer indexer;

    public IndexerResultEventWebsocket(URI serverUri, Indexer indexer) {
        super(serverUri);
        this.indexer = indexer;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
    }

    @Override
    public void onMessage(String s) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            IndexDocumentCommand command = mapper.readValue(s, IndexDocumentCommand.class);

            Document doc = command.document;
            indexer.indexDocument(doc);

            IndexerResultEvent event = new IndexerResultEvent(doc);
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
