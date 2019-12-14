package service;


import entity.WebSocketEntity;
import helper.ConvertJsonStringObject;
import model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;

public abstract class EventBusCommunication {
    public static Logger logger = LogManager.getLogger(EventBusCommunication.class);
    WebSocketEntity WSReceiver;
    WebSocketEntity WSSender;

    EventBusCommunication(URI uriReceiver, URI uriSender) {
        WSReceiver = new WebSocketEntity(uriReceiver);
        WSSender = new WebSocketEntity(uriSender);
    }

    EventBusCommunication(URI uri) {
        WSReceiver = new WebSocketEntity(uri);
        WSSender = null;
    }

    public void startEventLoop() {
        while (true) {
            if (WSReceiver.messageReceivedQueue.size() > 0) {
                String s = WSReceiver.messageReceivedQueue.poll();
                Message m = ConvertJsonStringObject.convertToMessage(s);
                if (m != null)
                    processMessage(m);
            }
            else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("[EVENTCOM] Thread sleep failed");
                }
            }
        }
    }
g
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
            WebSocketEntity webSocketEntity = new WebSocketEntity(uri);
            Message m = new Message(String.class.getName(), uuid);
            logger.error("[EVENTCOM] Send connection event for uuid : " + uuid);
            webSocketEntity.send(ConvertJsonStringObject.convertToJsonString(m));
            webSocketEntity.closeBlocking();
        } catch (InterruptedException e) {
            logger.error("[EVENTCOM] Websocket closing failed");
        }
    }
}
