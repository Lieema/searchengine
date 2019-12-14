package com.epita.domain.websocket.command;

import com.epita.clientapi.model.Message;
import com.epita.clientapi.service.EventBusCommunication;
import com.epita.domain.Domain;
import com.epita.utils.annotation.NotNull;
import com.epita.utils.annotation.Pure;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class CrawlUrlCommandWS extends EventBusCommunication {

    @NotNull
    private Domain domain;

    public CrawlUrlCommandWS(URI uri, Domain domain) {
        super(uri);
        this.domain = domain;
    }


    @Pure
    @Override
    public void processMessage(@NotNull final Message m) {
    }

    @Pure
    public void sendCrawnCommand(@NotNull final String url) {

        try {
            final String json = new ObjectMapper().writeValueAsString(url);
            final Message m  = new Message(String.class.getName(), json);
            sendMessage(m);
        } catch (JsonProcessingException e) {
        }
    }
}
