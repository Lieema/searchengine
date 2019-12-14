package com.epita.domain.websocket.connection;

import annotation.NotNull;
import annotation.Pure;
import com.epita.domain.Domain;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Message;
import service.EventBusCommunication;

import java.io.IOException;
import java.net.URI;

public class DomainCrawlerConnectionWS extends EventBusCommunication {

    @NotNull private Domain domain;

    public DomainCrawlerConnectionWS(@NotNull URI uriReceiver, @NotNull Domain domain) {
        super(uriReceiver);
        this.domain = domain;
    }


    @Pure
    @Override
    public void processMessage(@NotNull final Message m) {
        try {
            final String id = new ObjectMapper().readValue(m.jsonContent, String.class);
            domain.addCrawler(id);
        } catch (IOException e) {
        }
    }
}
