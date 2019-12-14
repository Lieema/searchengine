package com.epita.domain.websocket.result;

import com.epita.clientapi.model.Message;
import com.epita.clientapi.service.EventBusCommunication;
import com.epita.domain.Domain;

import java.net.URI;

public class IndexerResultEventWS extends EventBusCommunication {

    private Domain domain;

    public IndexerResultEventWS(URI uri, Domain domain) {
        super(uri);
        this.domain = domain;
    }

    @Override
    public void processMessage(Message m) {
        domain.updateIndexerQueue(m.senderUID);
    }
}
