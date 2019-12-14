package com.epita.eventbus.core;

import com.epita.eventbus.entity.Broker;
import com.mti.hivers.impl.Hivers;
import com.mti.hivers.impl.provider.Singleton;

public class Server {
    public static void main(String[] args) {

        Hivers hiver = new Hivers();
        hiver.addProvider(new Singleton<>(Broker.class, new Broker(8080)));
    }
}
