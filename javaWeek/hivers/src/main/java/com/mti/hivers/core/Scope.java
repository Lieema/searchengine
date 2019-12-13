package com.mti.hivers.core;


import annotation.Mutate;
import annotation.NotNull;
import annotation.Pure;

import java.util.List;
import java.util.Optional;

public interface Scope {

    @NotNull
    @Pure
    List<Provider> getProviderList();

    @NotNull
    @Mutate
    <@NotNull BEAN_TYPE> Scope addProvider(@NotNull final Provider<BEAN_TYPE> provider);

    @NotNull
    @Pure
    <@NotNull BEAN_TYPE>
    Optional<Provider<BEAN_TYPE>> getProviderForClass(@NotNull final Class<BEAN_TYPE> beanTypeClass);
}
