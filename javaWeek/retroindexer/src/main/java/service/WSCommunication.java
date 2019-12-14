package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.RetroIndex;
import model.Document;
import model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;

public class WSCommunication extends EventBusCommunication {
    public Logger logger = LogManager.getLogger(WSCommunication.class);
    public RetroIndex retroIndex;

    public WSCommunication(URI uri) {
        super(uri);
        retroIndex = new RetroIndex();
    }

    @Override
    public void processMessage(Message m) {
        try {
            Document doc =  new ObjectMapper().readValue(m.jsonContent, Document.class);
            logger.info("[WSCOM] Add new document to retro index : " + doc.link);
            retroIndex.addDocument(doc);
        } catch (Exception e) {
            logger.error("[WSCOM] Document deserialization failed");
        }
    }
}
