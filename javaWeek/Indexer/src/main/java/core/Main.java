package core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.WSCommunication;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

public class Main {
    public static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        WSCommunication wsCommunication = null;
        try {
            wsCommunication = new WSCommunication(
                    new URI("ws://localhost:8080/subscribe/broadcast/index_document_command/" + UUID.randomUUID().toString()),
                    new URI("ws://localhost:8080/subscribe/broadcast/index_result_event"));
            wsCommunication.startEventLoop();
        } catch (URISyntaxException e) {
            logger.error("[MAIN] Error parsing URI");
        }
    }
}
