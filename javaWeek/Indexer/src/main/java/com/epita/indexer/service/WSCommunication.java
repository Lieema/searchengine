package com.epita.indexer.service;

import com.epita.clientapi.service.EventBusCommunication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.epita.tfidf.model.Document;
import com.epita.clientapi.model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epita.tfidf.service.indexService.DefaultIndexerService;

import java.net.URI;
import java.net.URISyntaxException;

public class WSCommunication extends EventBusCommunication {
    public Logger logger = LogManager.getLogger(WSCommunication.class);
    private DefaultIndexerService indexerService = new DefaultIndexerService();
    private String uuid;

    public WSCommunication(URI uriReceiver, URI uriSender, String uuid) {
        super(uriReceiver, uriSender);
        this.uuid = uuid;
        try {
            sendConnectionEvent(new URI("ws://localhost:8080/subscribe/broadcast/indexer_connection_event"), uuid);
        } catch (URISyntaxException e) {
            logger.error("[WSCOM] Uri parsing failed");
        }
        logger.info("[WSCOM] New websocket client (receiver and sender) with uuid : " + uuid);
    }

    @Override
    public void processMessage(Message m) {
        try {
            String url =  new ObjectMapper().readValue(m.jsonContent, String.class);
            logger.info("[WSCOM] Start indexer process : " + url);
            sendDocument(indexerService.getDocumentFromLink(url));
        } catch (Exception e) {
            logger.error("[WSCOM] Document deserialization failed");
        }
    }

    public void sendDocument(Document document) {
        String content = null;
        try {
            //document.html = document.html.substring(0, Math.min(1000, document.html.length()));
            //document.text = document.text.substring(0, Math.min(1000, document.text.length()));
            document.html = "";
            document.text = "";
            content = new ObjectMapper().writeValueAsString(document);
            String className = Document.class.getName();
            sendMessage(new Message(className, content, uuid));
        } catch (JsonProcessingException e) {
            logger.error("[WSCOM] Document serialization failed");
        }
    }
}
