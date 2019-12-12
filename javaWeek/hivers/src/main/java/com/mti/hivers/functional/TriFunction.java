package com.mti.hivers.functional;

public interface TriFunction<P1, P2, P3, RET> {

    RET apply(final P1 p1, final P2 p2, final P3 p3);
}
