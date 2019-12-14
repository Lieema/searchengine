package com.epita.domain.event;

public class CrawlerConnectionEvent implements Event {

    public String id;

    public String topicName;

    public CrawlerConnectionEvent(String id, String topicName) {
        this.id = id;
        this.topicName = topicName;
    }
}
