package com.epita.domain.websocket.command;

import annotation.NotNull;
import annotation.Pure;
import com.epita.domain.Domain;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Message;
import service.EventBusCommunication;

import java.io.IOException;
import java.net.URI;

public class CrawlUrlCommandWS extends EventBusCommunication {

    private Domain domain;

    public CrawlUrlCommandWS(URI uriReceiver, URI uriSender, Domain domain) {
        super(uriReceiver, uriSender);
        this.domain = domain;
    }

    @Override
    public void processMessage(@NotNull final Message m) {

        try {
            String url = new ObjectMapper().readValue(m.jsonContent, String.class);
            domain.addUrlToIndex(url);
            domain.updateCrawlerQueue(m.senderUID);
        } catch (IOException e) {
        }

    }

    @Pure
    public void sendCrawnCommand(@NotNull final String url) {

        try {
            String json = new ObjectMapper().writeValueAsString(url);
            Message m  = new Message(String.class.getName(), json);
            sendMessage(m);
        } catch (JsonProcessingException e) {
        }
    }
}
