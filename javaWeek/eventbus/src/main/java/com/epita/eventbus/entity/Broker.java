package com.epita.eventbus.entity;

import io.javalin.Javalin;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsMessageContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class Broker {
    public Logger logger = LogManager.getLogger(Broker.class);

    HashMap<String, Topic> topics;
    Javalin app;

    public Broker(int port) {
        this.app = Javalin.create().start(port);
        this.topics = new HashMap<>();

        initBroadcastWS();
        initPublishWS();
        try {
            brokerEvents();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initBroadcastWS() {
        logger.info("[BROADCAST] Init websocket");
        app.ws("/subscribe/broadcast/:topic", ws -> {
            ws.onConnect(ctx -> {
                String topicName = ctx.pathParam("topic");
                logger.info("[BROADCAST][" + topicName + "] Client connected : " + ctx.getSessionId());
                if (!topics.containsKey(topicName))
                    topics.putIfAbsent(topicName, new Topic(topicName));
                Topic topic = topics.get(topicName);
                topic.clients.add(ctx);
            });
            ws.onMessage(ctx -> {
                String topicName = ctx.pathParam("topic");
                logger.info("[BROADCAST][" + topicName + "] Got message from client : " + ctx.getSessionId());
                if (!topics.containsKey(topicName)) {
                    logger.debug("[BROADCAST][" + topicName + "] Create topic");
                    topics.putIfAbsent(topicName, new Topic(topicName));
                }
                Topic topic = topics.get(topicName);
                logger.debug("[BROADCAST][" + topicName + "] Add client " + ctx.getSessionId() + " to topic");
                topic.broadcastQueue.add(ctx);
            });
            ws.onClose(ctx -> {
                logger.info("[BROADCAST][" + ctx.pathParam("topic") + "] Client disconnected : " + ctx.getSessionId());
                String topicName = ctx.pathParam("topic");
                if (topics.containsKey(topicName)) {
                    Topic topic = topics.get(topicName);
                    logger.debug("[BROADCAST][" + topicName + "] Remove client " + ctx.getSessionId() + " from topic");
                    topic.clients.remove(ctx);
                }
            });
            ws.onError(ctx -> logger.error("[BROADCAST] Client error : " + ctx.getSessionId()));
        });
    }

    private void initPublishWS() {
        logger.info("[PUBLISH] Init websocket");
        app.ws("/subscribe/publish/:topic", ws -> {
            ws.onConnect(ctx -> {
                String topicName = ctx.pathParam("topic");
                logger.info("[PUBLISH][" + topicName + "] Client connected : " + ctx.getSessionId());
                if (!topics.containsKey(topicName)) {
                    logger.debug("[PUBLISH][" + topicName + "] Create topic");
                    topics.putIfAbsent(topicName, new Topic(topicName));
                }
                Topic topic = topics.get(topicName);
                logger.debug("[PUBLISH][" + topicName + "] Add client " + ctx.getSessionId() + " to topic");
                topic.clients.add(ctx);
            });
            ws.onMessage(ctx -> {
                logger.info("[PUBLISH][" + ctx.pathParam("topic") + "] Got message from client : " + ctx.getSessionId());
                String topicName = ctx.pathParam("topic");
                if (!topics.containsKey(topicName))
                    topics.putIfAbsent(topicName, new Topic(topicName));
                Topic topic = topics.get(topicName);
                topic.publishQueue.add(ctx);
            });
            ws.onClose(ctx -> {
                logger.info("[PUBLISH][" + ctx.pathParam("topic") + "] Client disconnected : " + ctx.getSessionId());
                String topicName = ctx.pathParam("topic");
                if (topics.containsKey(topicName)) {
                    Topic topic = topics.get(topicName);
                    logger.debug("[PUBLISH][" + topicName + "] Remove client " + ctx.getSessionId() + " from topic");
                    topic.clients.remove(ctx);
                }
            });
            ws.onError(ctx -> logger.error("[PUBLISH] Client error : " + ctx.getSessionId()));
        });
    }

    private void brokerEvents() throws InterruptedException {
        logger.info("[BROKER] Start event loop");
        int publishIndex = 0;
        boolean allQueuesEmpty;
        while (true) {
            allQueuesEmpty = true;
            for (Topic t : topics.values()) {
                if (t.broadcastQueue.size() > 0) {
                    WsMessageContext ctx = t.broadcastQueue.pop();
                    for (WsConnectContext client : t.clients)
                        if (!ctx.getSessionId().equals(client.getSessionId())) {
                            logger.info("[BROKER][" + t.name + "] Broadcast message from " + ctx.getSessionId());
                            logger.debug("[BROKER][" + t.name + "] message : " + ctx.message());
                            client.send(ctx.message());
                        }
                    if (t.broadcastQueue.size() > 0)
                        allQueuesEmpty = false;
                }
                else
                    logger.trace("[BROKER] No messages in broadcastQueue");
                if (t.publishQueue.size() > 0) {
                    WsMessageContext ctx = t.publishQueue.pop();
                    WsConnectContext client = t.clients.get(publishIndex);
                    publishIndex = (publishIndex + 1) % t.clients.size();
                    if (!ctx.getSessionId().equals(client.getSessionId())) {
                        logger.info("[BROKER][" + t.name + "] Publish message from " + ctx.getSessionId());
                        logger.debug("[BROKER][" + t.name + "] message : " + ctx.message());
                        client.send(ctx.message());
                    }
                    else if (t.clients.size() > 1){
                        t.publishQueue.push(ctx);
                    }
                    if (t.publishQueue.size() > 0)
                        allQueuesEmpty = false;
                }
                else
                    logger.trace("[BROKER] No messages in publishQueue");
            }
            if (allQueuesEmpty) {
                logger.trace("[BROKER] Waiting 1 second and retrying.");
                Thread.sleep(1000);
            }
        }
    }
}
