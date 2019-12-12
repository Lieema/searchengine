package com.mti.hivers.functional;

@FunctionalInterface
public interface ThrowingTriConsumer<A, B, C, EXCEPTION extends Throwable> {

    void apply(A a, B b, C c) throws EXCEPTION;
}
