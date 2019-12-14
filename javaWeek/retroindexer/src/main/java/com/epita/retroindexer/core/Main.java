package com.epita.retroindexer.core;

import com.epita.retroindexer.service.RestApi;
import com.epita.retroindexer.service.WSCommunication;
import com.mti.hivers.impl.Hivers;
import com.mti.hivers.impl.provider.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;

public class Main {
    public static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        Hivers hiver = new Hivers();

        hiver.addProvider(new Singleton<>(RestApi.class, new RestApi(8000)));

        try {
            WSCommunication ws = new WSCommunication(new URI("ws://localhost:8080/subscribe/broadcast/index_result_event"));
            hiver.addProvider(new Singleton<>(WSCommunication.class, ws));
            hiver.instanceOf(WSCommunication.class).get().startWS();

        } catch (URISyntaxException e) {
            logger.error("[MAIN] Error parsing URI");
        }
    }
}
