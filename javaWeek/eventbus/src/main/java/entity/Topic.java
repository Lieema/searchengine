package entity;

import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsMessageContext;

import java.util.ArrayDeque;
import java.util.Deque;

public class Topic {
    public String name;
    public Deque<WsMessageContext> broadcastQueue = new ArrayDeque<>();
    public Deque<WsMessageContext> publishQueue = new ArrayDeque<>();
    public Deque<WsConnectContext> clients = new ArrayDeque<>();

    public Topic(String name) {
        this.name = name;
    }
}
