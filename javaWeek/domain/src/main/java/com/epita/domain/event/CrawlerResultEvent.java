package com.epita.domain.event;

import model.Document;

public class CrawlerResultEvent implements Event {

    public Document document;

    public CrawlerResultEvent(Document document) {
        this.document = document;
    }
}
