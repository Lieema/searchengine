package com.mti.hivers.impl;

import com.epita.utils.annotation.NotNull;
import com.mti.hivers.core.Provider;
import com.mti.hivers.core.Scope;
import com.epita.utils.validation.Fault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DefaultScope implements Scope {

    private final List<Provider> providerList = new ArrayList<>();

    @Override
    @NotNull
    public List<Provider> getProviderList() {
        return Collections.unmodifiableList(providerList);
    }

    @Override
    @NotNull
    public <BEAN_TYPE> Scope addProvider(@NotNull final Provider<BEAN_TYPE> provider) {
        Fault.NULL.validate(provider, "provider");
        providerList.add(provider);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    @NotNull
    public <BEAN_TYPE> Optional<Provider<BEAN_TYPE>> getProviderForClass(final Class<BEAN_TYPE> beanTypeClass) {
        Fault.NULL.validate(beanTypeClass, "beanTypeClass");

        return providerList.stream()
                .filter(provider -> beanTypeClass.isAssignableFrom(provider.getProvidedClass()))
                .map(provider -> (Provider<BEAN_TYPE>) provider)
                .findFirst();
    }
}
