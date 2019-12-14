package com.epita.domain.event;

public class IndexerConnectionEvent implements Event {

    public String id;

    public String topicName;

    public IndexerConnectionEvent(String id, String topicName) {
        this.id = id;
        this.topicName = topicName;
    }

}
