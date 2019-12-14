package com.epita.domain.websocket.connection;

import com.epita.clientapi.model.Message;
import com.epita.clientapi.service.EventBusCommunication;
import com.epita.domain.Domain;
import com.epita.utils.annotation.NotNull;
import com.epita.utils.annotation.Pure;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;

public class DomainIndexerConnectionWS extends EventBusCommunication {

    @NotNull
    private Domain domain;

    public DomainIndexerConnectionWS(@NotNull URI uri, @NotNull Domain domain) {
        super(uri);
        this.domain = domain;
    }

    @Pure
    @Override
    public void processMessage(@NotNull final Message m) {
        try {
            final String id = new ObjectMapper().readValue(m.jsonContent, String.class);
            domain.addIndexer(id);
        } catch (IOException e) {
        }
    }
}
