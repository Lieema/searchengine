package com.mti.hivers.impl.aspect;

import annotation.NotNull;
import com.mti.hivers.core.Aspect;
import com.mti.hivers.core.Provider;
import com.mti.hivers.functional.TriConsumer;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Before<BEAN_TYPE> implements Aspect<BEAN_TYPE> {

    private final TriConsumer<BEAN_TYPE, Method, Object[]> aspect;

    public Before(final TriConsumer<BEAN_TYPE, Method, Object[]> aspect) {
        this.aspect = aspect;
    }

    @SuppressWarnings("unchecked")
    public BEAN_TYPE proxify(@NotNull final Provider<BEAN_TYPE> provider,
                             @NotNull final BEAN_TYPE bean) {

        return (BEAN_TYPE) Proxy.newProxyInstance(
                bean.getClass().getClassLoader(),
                new Class[]{provider.getProvidedClass()},
                (proxy, method, args) -> {
                    aspect.apply(bean, method, args);
                    return method.invoke(bean, args);
                });
    }
}
