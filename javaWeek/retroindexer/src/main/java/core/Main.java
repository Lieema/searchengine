package core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.RestApi;
import service.WSCommunication;

import java.net.URI;
import java.net.URISyntaxException;

public class Main {
    public static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        RestApi api = new RestApi(8000);
        try {
            WSCommunication ws = new WSCommunication(new URI("ws://localhost:8080/subscribe/broadcast/index_result_event"));
            ws.startEventLoop();
        } catch (URISyntaxException e) {
            logger.error("[MAIN] Error parsing URI");
        }
    }
}
