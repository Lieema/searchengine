package com.epita.clientapi.entity;

import com.epita.clientapi.helper.ConvertJsonStringObject;
import com.epita.clientapi.model.Message;
import com.epita.clientapi.service.EventBusCommunication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.LinkedList;
import java.util.Queue;

public class WebSocketEntity extends WebSocketClient {
    private Logger logger = LogManager.getLogger(getClass());

    private EventBusCommunication eventBusCommunication;

    public WebSocketEntity(URI serverUri, EventBusCommunication eventBusCommunication) {
        super(serverUri);
        this.eventBusCommunication = eventBusCommunication;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.info("[WS] Connect to " + getURI().toString());
    }


    @Override
    public void onClose(int i, String s, boolean b) {
        logger.info("[WS] Disconnect from " + getURI().toString());
    }

    @Override
    public void onError(Exception e) {
        logger.error("[WS] Error on connection");
    }

    @Override
    public void onMessage(String s) {
        logger.info("[WS] Receive message, added to queue");
        logger.debug("[WS] message : " + s);
        Message m = ConvertJsonStringObject.convertToMessage(s);
        if (m != null)
            eventBusCommunication.processMessage(m);
    }

    @Override
    public void send(String text) {
        logger.info("[WS] Send message");
        logger.debug("[WS] message : " + text);
        super.send(text);
    }


}
