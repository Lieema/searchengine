package com.epita.domain.websocket.connection;

import com.epita.domain.Domain;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Message;
import service.EventBusCommunication;

import java.io.IOException;
import java.net.URI;

public class DomainIndexerConnectionWS extends EventBusCommunication {

    private Domain domain;

    public DomainIndexerConnectionWS(URI uri, Domain domain) {
        super(uri);
        this.domain = domain;
    }

    @Override
    public void processMessage(Message m) {
        try {
            String id = new ObjectMapper().readValue(m.jsonContent, String.class);
            domain.addIndexer(id);
        } catch (IOException e) {
        }
    }
}
