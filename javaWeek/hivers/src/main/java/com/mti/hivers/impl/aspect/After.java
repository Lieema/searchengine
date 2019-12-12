package com.mti.hivers.impl.aspect;

import com.mti.hivers.annotation.NotNull;
import com.mti.hivers.core.Aspect;
import com.mti.hivers.core.Provider;
import com.mti.hivers.functional.ThrowingTriConsumer;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class After<BEAN_TYPE> implements Aspect<BEAN_TYPE> {

    private final ThrowingTriConsumer<BEAN_TYPE, Method, Object[], Throwable> aspect;

    public After(final ThrowingTriConsumer<BEAN_TYPE, Method, Object[], Throwable> aspect) {
        this.aspect = aspect;
    }

    @SuppressWarnings("unchecked")
    public BEAN_TYPE proxify(@NotNull final Provider<BEAN_TYPE> provider,
                             @NotNull final BEAN_TYPE bean) {

        return (BEAN_TYPE) Proxy.newProxyInstance(
                bean.getClass().getClassLoader(),
                new Class[]{provider.getProvidedClass()},
                (proxy, method, args) -> {
                    final Object result = method.invoke(bean, args);
                    aspect.apply(bean, method, args);
                    return result;
                });
    }
}
