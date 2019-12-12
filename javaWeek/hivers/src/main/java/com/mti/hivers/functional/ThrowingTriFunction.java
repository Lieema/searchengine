package com.mti.hivers.functional;

public interface ThrowingTriFunction<P1, P2, P3, RET, EXCEPTION_TYPE extends Throwable> {

    RET apply(final P1 p1, final P2 p2, final P3 p3) throws EXCEPTION_TYPE;
}
