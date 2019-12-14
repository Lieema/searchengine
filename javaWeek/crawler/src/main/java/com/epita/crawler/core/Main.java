package com.epita.crawler.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.epita.crawler.service.WSCommunication;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

public class Main {
    public static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            String uuid = UUID.randomUUID().toString();
            WSCommunication wsCommunication = new WSCommunication(
                    new URI("ws://localhost:8080/subscribe/broadcast/crawler_url_command/" + uuid),
                    new URI("ws://localhost:8080/subscribe/broadcast/crawler_result_event"),
                    uuid);
            wsCommunication.startEventLoop();
        } catch (URISyntaxException e) {
            logger.error("[MAIN] Error parsing URI");
        }
    }
}
