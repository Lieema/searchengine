package com.epita.domain;

import com.mti.hivers.impl.Hivers;
import com.mti.hivers.impl.provider.Singleton;

public class Main {

    public static void main(String[] args) {

        Hivers hiver = new Hivers();

        hiver.addProvider(new Singleton<>(Domain.class, new Domain()));

        hiver.instanceOf(Domain.class).get().startEventLoop();
    }
}
