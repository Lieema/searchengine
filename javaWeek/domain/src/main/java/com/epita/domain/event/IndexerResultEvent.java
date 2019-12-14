package com.epita.domain.event;

import model.Document;

public class IndexerResultEvent implements Event {

    public Document document;

    public IndexerResultEvent(Document document) {
        this.document = document;
    }
}
