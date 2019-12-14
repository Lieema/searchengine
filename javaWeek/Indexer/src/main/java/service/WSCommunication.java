package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Document;
import model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.indexService.DefaultIndexerService;

import java.net.URI;

public class WSCommunication extends EventBusCommunication {
    public Logger logger = LogManager.getLogger(WSCommunication.class);
    DefaultIndexerService indexerService = new DefaultIndexerService();

    public WSCommunication(URI uriReceiver, URI uriSender) {
        super(uriReceiver, uriSender);
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
            content = new ObjectMapper().writeValueAsString(document);
        } catch (JsonProcessingException e) {
            logger.error("[WSCOM] Document serialization failed");
        }
        String className = Document.class.getName();
        sendMessage(new Message(className, content));
    }
}
