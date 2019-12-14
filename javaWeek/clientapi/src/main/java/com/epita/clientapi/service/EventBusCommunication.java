package com.epita.clientapi.service;


import com.epita.clientapi.entity.WebSocketEntity;
import com.epita.clientapi.helper.ConvertJsonStringObject;
import com.epita.clientapi.model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;

public abstract class EventBusCommunication {
    public static Logger logger = LogManager.getLogger(EventBusCommunication.class);
    WebSocketEntity WSReceiver;
    WebSocketEntity WSSender;

    public EventBusCommunication(URI uriReceiver, URI uriSender) {
        WSReceiver = new WebSocketEntity(uriReceiver, this);
        WSSender = new WebSocketEntity(uriSender, this);
    }

    public EventBusCommunication(URI uri) {
        WSReceiver = new WebSocketEntity(uri, this);
        WSSender = null;
    }

    public void startWS() {
        try {
            if (WSReceiver != null) {
                logger.info("[EVENTCOM] Start websocket (receiver) : " + WSReceiver.getURI().toString());
                WSReceiver.connectBlocking();
            }
        } catch (InterruptedException e) {
            logger.error("[EVENTCOM] Error connecting to websocket : " + WSReceiver.getURI().toString());
        }
        try {
            if (WSSender != null) {
                logger.info("[EVENTCOM] Start websocket (sender) : " + WSSender.getURI().toString());
                WSSender.connectBlocking();
            }
        } catch (InterruptedException e) {
            logger.error("[EVENTCOM] Error connecting to websocket : " + WSSender.getURI().toString());
        }
    }

    public abstract void processMessage(Message m);

    public void sendMessage(Message message) {
        String json = ConvertJsonStringObject.convertToJsonString(message);
        if (WSSender == null)
            WSReceiver.send(json);
        else
            WSSender.send(json);
    }

    public void sendConnectionEvent(URI uri, String uuid) {
        try {
            WebSocketEntity webSocketEntity = new WebSocketEntity(uri, this);
            Message m = new Message(String.class.getName(), uuid, uuid);
            logger.error("[EVENTCOM] Send connection event for uuid : " + uuid);
            webSocketEntity.send(ConvertJsonStringObject.convertToJsonString(m));
            webSocketEntity.closeBlocking();
        } catch (InterruptedException e) {
            logger.error("[EVENTCOM] Websocket closing failed");
        }
    }
}
