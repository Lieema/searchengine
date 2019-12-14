package com.epita.eventbus.core;

import com.epita.eventbus.entity.Broker;

public class Server {
    public static void main(String[] args) {
        Broker broker = new Broker(8080);
    }
}
