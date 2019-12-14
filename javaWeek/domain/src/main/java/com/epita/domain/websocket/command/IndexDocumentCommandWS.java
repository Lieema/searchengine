package com.epita.domain.websocket.command;

import annotation.NotNull;
import annotation.Pure;
import com.epita.domain.Domain;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Document;
import model.Message;
import service.EventBusCommunication;

import java.net.URI;

public class IndexDocumentCommandWS extends EventBusCommunication {

    private Domain domain;

    public IndexDocumentCommandWS(URI uriReceiver, URI uriSender, Domain domain) {
        super(uriReceiver, uriSender);
        this.domain = domain;
    }

    @Override
    public void processMessage(Message m) {
        domain.updateIndexerQueue(m.senderUID);
    }

    @Pure
    public void sendCommand(@NotNull final Document document) {
        try {
            String json = new ObjectMapper().writeValueAsString(document);
            Message m  = new Message(Document.class.getName(), json);
            sendMessage(m);
        } catch (JsonProcessingException e) {
        }
    }
}
