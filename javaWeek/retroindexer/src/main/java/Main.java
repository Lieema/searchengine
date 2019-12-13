import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Document;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.RestApi;
import service.WSClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Main {
    public static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        RestApi api = new RestApi(8000);
        try {
            WSClient ws = new WSClient(new URI("ws://localhost:8080/subscribe/broadcast/retro_index_store_command"));
            ws.connectBlocking();
            while (true) {
                if (ws.documents.size() > 0) {
                    Document d = ws.documents.pop();
                    logger.info("[EVENTS] Add new document to retro index : " + d.link);
                    api.addDocument(d);
                }
                else
                    Thread.sleep(1000);
            }
        } catch (URISyntaxException e) {
            logger.error("[MAIN] Error parsing URI");
        } catch (InterruptedException e) {
            logger.error("[MAIN] Websocket connection failedI");
        }
    }
}
