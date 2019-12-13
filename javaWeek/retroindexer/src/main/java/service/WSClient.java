package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Document;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.Deque;

public class WSClient extends WebSocketClient {
    public Logger logger = LogManager.getLogger(WSClient.class);
    public Deque<Document> documents = new ArrayDeque<>();

    public WSClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.info("[WS] Connect to " + getURI().toString());
    }

    @Override
    public void onMessage(String s) {
        logger.info("[WS] Receive message");
        logger.debug("[WS] message : " + s);

        try {
            Document doc = new ObjectMapper().readValue(s, Document.class);
            documents.add(doc);
        } catch (Exception e) {
            logger.error("[WS] Deserialization failed");
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        logger.info("[WS] Disconnect from " + getURI().toString());
    }

    @Override
    public void onError(Exception e) {
        logger.error("[WS] Error on connection");
    }
}
