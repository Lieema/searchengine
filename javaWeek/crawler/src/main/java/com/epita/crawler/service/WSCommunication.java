package com.epita.crawler.service;

import com.epita.clientapi.service.EventBusCommunication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.epita.clientapi.model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class WSCommunication extends EventBusCommunication {
    private Logger logger = LogManager.getLogger(WSCommunication.class);
    private String uuid;

    public WSCommunication(URI uriReceiver, URI uriSender, String uuid) {
        super(uriReceiver, uriSender);
        this.uuid = uuid;
        try {
            sendConnectionEvent(new URI("ws://localhost:8080/subscribe/broadcast/crawler_connection_event"), uuid);
        } catch (URISyntaxException e) {
            logger.error("[WSCOM] Uri parsing failed");
        }
        logger.info("[WSCOM] New websocket client (receiver and sender) with uuid : " + uuid);
    }

    public void processMessage(Message m) {
        try {
            String url =  new ObjectMapper().readValue(m.jsonContent, String.class);
            logger.info("[WSCOM] Start crawler process : " + url);
            Crawler crawler = new Crawler(url);
            crawler.crawl();
            sendUrls(crawler.getFoundUrls());
        } catch (Exception e) {
            logger.error("[WSCOM] Document deserialization failed");
        }
    }

    public void sendUrls(List<String> urls) {
        String content = null;
        try {
            content = new ObjectMapper().writeValueAsString(urls);
        } catch (JsonProcessingException e) {
            logger.error("[WSCOM] Urls serialization failed");
        }
        String className = List.class.getName();
        sendMessage(new Message(className, content, uuid));
    }
}
