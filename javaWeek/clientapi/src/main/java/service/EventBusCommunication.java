package service;


import entity.WebSocketEntity;
import helper.ConvertJsonStringObject;
import model.Message;

import java.net.URI;

public abstract class EventBusCommunication {
    WebSocketEntity webSocketEntity;

    EventBusCommunication(URI uri) {
        webSocketEntity = new WebSocketEntity(uri);
    }

    public void subscribe(String senderType) {
        Message toSend = new Message();
        toSend.senderType = senderType;
        toSend.messageType = "subscribe";
        ConvertJsonStringObject converter = new ConvertJsonStringObject();

        webSocketEntity.send(converter.convertToJsonString(toSend));
    }

    public void sendMessage(String messageType, String senderType, String message) {
        Message toSend = new Message();
        toSend.senderType = senderType;
        toSend.messageType = messageType;
        toSend.message = message;
        ConvertJsonStringObject converter = new ConvertJsonStringObject();

        webSocketEntity.send(converter.convertToJsonString(toSend));
    }

    public void unsubscribe(String senderType) {
        Message toSend = new Message();
        toSend.senderType = senderType;
        toSend.messageType = "unsubscribe";
        ConvertJsonStringObject converter = new ConvertJsonStringObject();

        webSocketEntity.send(converter.convertToJsonString(toSend));
    }
}
