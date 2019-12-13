package com.mti.hivers.impl.provider;

import annotation.NotNull;
import com.mti.hivers.core.Aspect;
import com.mti.hivers.core.Provider;
import com.mti.hivers.impl.aspect.After;
import com.mti.hivers.impl.aspect.Around;
import com.mti.hivers.impl.aspect.Before;
import exception.ProxyTypeNotAnInterfaceException;
import validation.Assert;
import validation.Fault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractProvider<BEAN_TYPE> implements Provider<BEAN_TYPE> {

    @NotNull
    public final Class<BEAN_TYPE> providedClass;

    @NotNull
    public final List<Aspect<BEAN_TYPE>> aspectList = new ArrayList<>();

    public AbstractProvider(@NotNull final Class<BEAN_TYPE> providedClass,
                            @NotNull final List<Aspect<BEAN_TYPE>> aspectList) {

        this.providedClass = Fault.NULL.validate(providedClass, "providedClass");
        this.aspectList.addAll(Fault.validate(aspectList, "aspectList", Fault.NULL, Fault.NO_NULL_IN_COLLECTION));
    }

    private List<Aspect<BEAN_TYPE>> aspectsOfType(final Class<?> aspectType, final boolean reversed) {
        final var list = aspectList.stream().filter(aspectType::isInstance).collect(Collectors.toList());
        if (reversed) Collections.reverse(list);
        return list;
    }

    @NotNull
    @Override
    public Class<BEAN_TYPE> getProvidedClass() {
        return providedClass;
    }

    @Override
    public List<Aspect> getAspectList() {
        return Collections.unmodifiableList(aspectList);
    }

    @SuppressWarnings("unchecked")
    protected BEAN_TYPE proxify(final BEAN_TYPE bean) {

        if (aspectList.isEmpty()) {
            return bean;
        }

        Assert.isTrue(providedClass.isInterface(), ProxyTypeNotAnInterfaceException::new);

        BEAN_TYPE proxied = bean;


        for (final Aspect<BEAN_TYPE> aspect : aspectsOfType(Around.class, true)) {
            proxied = aspect.proxify(this, proxied);
        }


        for (final Aspect<BEAN_TYPE> aspect : aspectsOfType(After.class, false)) {
            proxied = aspect.proxify(this, proxied);
        }

        for (final Aspect<BEAN_TYPE> aspect : aspectsOfType(Before.class, true)) {
            proxied = aspect.proxify(this, proxied);
        }

        return proxied;
    }


}
