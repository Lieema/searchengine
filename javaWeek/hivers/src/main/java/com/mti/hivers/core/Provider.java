package com.mti.hivers.core;

import annotation.NotNull;
import annotation.Nullable;

import java.util.List;

public interface Provider<@NotNull BEAN_TYPE> {

    @Nullable
    BEAN_TYPE provide();

    @NotNull
    Class<BEAN_TYPE> getProvidedClass();

    List<Aspect> getAspectList();
}
