package com.epita.domain.websocket.result;

import com.epita.clientapi.model.Message;
import com.epita.clientapi.service.EventBusCommunication;
import com.epita.domain.Domain;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class CrawlerResultEventWS extends EventBusCommunication {

    private Domain domain;

    public CrawlerResultEventWS(URI uri, Domain domain) {
        super(uri);
        this.domain = domain;
    }

    @Override
    public void processMessage(Message m) {
        try {
            final List<String> urls = new ObjectMapper().readValue(m.jsonContent, List.class);
            domain.addUrlToCrawl(urls);
            domain.updateCrawlerQueue(m.senderUID);
        } catch (IOException e) {
        }
    }
}
