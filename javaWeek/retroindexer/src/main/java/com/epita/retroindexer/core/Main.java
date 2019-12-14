package com.epita.retroindexer.core;

import com.epita.retroindexer.service.RestApi;
import com.epita.retroindexer.service.WSCommunication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;

public class Main {
    public static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        RestApi api = new RestApi(8000);
        try {
            WSCommunication ws = new WSCommunication(new URI("ws://localhost:8080/subscribe/broadcast/index_result_event"));
        } catch (URISyntaxException e) {
            logger.error("[MAIN] Error parsing URI");
        }
    }
}
