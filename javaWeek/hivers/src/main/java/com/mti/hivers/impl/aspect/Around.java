package com.mti.hivers.impl.aspect;

import com.mti.hivers.annotation.NotNull;
import com.mti.hivers.core.Aspect;
import com.mti.hivers.core.Provider;
import com.mti.hivers.functional.ThrowingTriFunction;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Around<BEAN_TYPE> implements Aspect<BEAN_TYPE> {
    private final ThrowingTriFunction<BEAN_TYPE, Method, Object[], Object, Exception> aspect;

    public Around(final ThrowingTriFunction<BEAN_TYPE, Method, Object[], Object, Exception> aspect) {
        this.aspect = aspect;
    }

    @SuppressWarnings("unchecked")
    public BEAN_TYPE proxify(@NotNull final Provider<BEAN_TYPE> provider,
                             @NotNull final BEAN_TYPE bean) {

        return (BEAN_TYPE) Proxy.newProxyInstance(
                bean.getClass().getClassLoader(),
                new Class[]{provider.getProvidedClass()},
                (proxy, method, args) -> aspect.apply(bean, method, args));
    }
}
