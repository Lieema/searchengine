package com.epita.eventbus.entity;

import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsMessageContext;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Topic {
    public String name;
    public Deque<WsMessageContext> broadcastQueue = new ArrayDeque<>();
    public Deque<WsMessageContext> publishQueue = new ArrayDeque<>();
    public List<WsConnectContext> clients = new ArrayList<>();

    public Topic(String name) {
        this.name = name;
    }
}
