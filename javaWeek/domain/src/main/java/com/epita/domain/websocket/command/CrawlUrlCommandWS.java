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

    public CrawlUrlCommandWS(@NotNull URI uriReceiver, @NotNull URI uriSender, @NotNull Domain domain) {
        super(uriReceiver, uriSender);
        this.domain = domain;
    }

    @Pure
    @Override
    public void processMessage(@NotNull final Message m) {

        try {
            final List<String> urls = new ObjectMapper().readValue(m.jsonContent, List.class);
            domain.addUrlToCrawl(urls);
            domain.updateCrawlerQueue(m.senderUID);
        } catch (IOException e) {
        }

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
