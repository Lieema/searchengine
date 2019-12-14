package com.epita.domain.websocket.command;

import com.epita.clientapi.model.Message;
import com.epita.clientapi.service.EventBusCommunication;
import com.epita.domain.Domain;
import com.epita.tfidf.model.Document;
import com.epita.utils.annotation.NotNull;
import com.epita.utils.annotation.Pure;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;

public class IndexDocumentCommandWS extends EventBusCommunication {

    @NotNull
    private Domain domain;

    public IndexDocumentCommandWS(@NotNull URI uriReceiver, @NotNull URI uriSender, @NotNull Domain domain) {
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
