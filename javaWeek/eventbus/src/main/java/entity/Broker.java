package entity;

import io.javalin.Javalin;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsMessageContext;

import java.util.HashMap;

public class Broker {
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
        app.ws("/subscribe/broadcast/:topic", ws -> {
            ws.onConnect(ctx -> {
                String topicName = ctx.pathParam("topic");
                System.out.println("[BROADCAST][" + topicName + "] Client connected : " + ctx.getSessionId());
                if (!topics.containsKey(topicName))
                    topics.putIfAbsent(topicName, new Topic(topicName));
                Topic topic = topics.get(topicName);
                topic.clients.add(ctx);
            });
            ws.onMessage(ctx -> {
                String topicName = ctx.pathParam("topic");
                System.out.println("[BROADCAST][" + topicName + "] Got message from client : " + ctx.getSessionId());
                if (!topics.containsKey(topicName))
                    topics.putIfAbsent(topicName, new Topic(topicName));
                Topic topic = topics.get(topicName);
                topic.broadcastQueue.add(ctx);
            });
            ws.onClose(ctx -> {
                System.out.println("[BROADCAST][" + ctx.pathParam("topic") + "] Client disconnected : " + ctx.getSessionId());
                String topicName = ctx.pathParam("topic");
                if (topics.containsKey(topicName)) {
                    Topic topic = topics.get(topicName);
                    topic.clients.remove(ctx);
                }
            });
            ws.onError(ctx -> System.out.println("Client error"));
        });
    }

    private void initPublishWS() {
        app.ws("/subscribe/publish/:topic", ws -> {
            ws.onConnect(ctx -> {
                String topicName = ctx.pathParam("topic");
                System.out.println("[PUBLISH][" + topicName + "] Client connected : " + ctx.getSessionId());
                if (!topics.containsKey(topicName))
                    topics.putIfAbsent(topicName, new Topic(topicName));
                Topic topic = topics.get(topicName);
                topic.clients.add(ctx);
            });
            ws.onMessage(ctx -> {
                System.out.println("[PUBLISH][" + ctx.pathParam("topic") + "] Got message from client : " + ctx.getSessionId());
                String topicName = ctx.pathParam("topic");
                if (!topics.containsKey(topicName))
                    topics.putIfAbsent(topicName, new Topic(topicName));
                Topic topic = topics.get(topicName);
                topic.publishQueue.add(ctx);
            });
            ws.onClose(ctx -> {
                System.out.println("[PUBLISH][" + ctx.pathParam("topic") + "] Client disconnected : " + ctx.getSessionId());
                String topicName = ctx.pathParam("topic");
                if (topics.containsKey(topicName)) {
                    Topic topic = topics.get(topicName);
                    topic.clients.remove(ctx);
                }
            });
            ws.onError(ctx -> System.out.println("Client error"));
        });
    }

    private void brokerEvents() throws InterruptedException {
        while (true) {
            for (Topic t : topics.values()) {
                if (t.broadcastQueue.size() > 0) {
                    WsMessageContext ctx = t.broadcastQueue.pop();
                    for (WsConnectContext client : t.clients)
                        if (!ctx.getSessionId().equals(client.getSessionId())) {
                            System.out.println("[BROKER][" + t.name + "] Broadcast message from " + ctx.getSessionId() + " : " + ctx.message());
                            client.send(ctx.message());
                        }
                }
                if (t.publishQueue.size() > 0) {
                    WsMessageContext ctx = t.publishQueue.pop();
                    WsConnectContext client = t.clients.pop();
                    t.clients.add(client);
                    if (!ctx.getSessionId().equals(client.getSessionId())) {
                        System.out.println("[BROKER][" + t.name + "] Publish message from " + ctx.getSessionId() + " : " + ctx.message());
                        client.send(ctx.message());
                    }
                    else if (t.clients.size() > 1){
                        t.publishQueue.push(ctx);
                    }
                }
            }
            Thread.sleep(1000);
        }
    }
}
