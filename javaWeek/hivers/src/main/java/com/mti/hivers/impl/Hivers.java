package com.mti.hivers.impl;

import annotation.Mutate;
import annotation.NotNull;
import annotation.Pure;
import com.mti.hivers.core.Provider;
import com.mti.hivers.core.Scope;
import com.mti.hivers.core.ScopeStack;
import exception.BeanNotFoundException;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static validation.Fault.NULL;

public class Hivers implements ScopeStack, Scope {

    private final Deque<Scope> scopeDeque = new ArrayDeque<>();

    public Hivers() {
        push(new DefaultScope());
    }

    @Override
    public Deque<Scope> getScopeStack() {
        return scopeDeque;
    }

    @Override
    public List<Provider> getProviderList() {
        return getScopeStack().stream()
                .flatMap(scope -> scope.getProviderList().stream())
                .collect(Collectors.toList());
    }

    @Override
    public <BEAN_TYPE> Scope addProvider(@NotNull final Provider<BEAN_TYPE> provider) {
        @NotNull Provider<BEAN_TYPE> notNullProvider = NULL.validate(provider, "provider");

        return peek().addProvider(provider);
    }

    @NotNull
    @Override
    public <BEAN_TYPE> Optional<Provider<BEAN_TYPE>> getProviderForClass(@NotNull final Class<BEAN_TYPE> beanTypeClass) {
        NULL.validate(beanTypeClass, "beanTypeClass");

        return Optional.ofNullable(getScopeStack().peek())
                .flatMap(scope -> scope.getProviderForClass(beanTypeClass));
    }

    @Pure
    @NotNull
    public <BEAN_TYPE> Optional<BEAN_TYPE> instanceOf(@NotNull final Class<BEAN_TYPE> beanTypeClass) {
        NULL.validate(beanTypeClass, "beanTypeClass");
        return getProviderForClass(beanTypeClass).map(Provider::provide);
    }

    @Pure
    @NotNull
    public <BEAN_TYPE> BEAN_TYPE instanceOfOrThrow(@NotNull final Class<BEAN_TYPE> beanTypeClass) {
        return instanceOf(beanTypeClass).orElseThrow(BeanNotFoundException::new);
    }

    @Mutate
    public void push() {
        push(new DefaultScope());
    }
}
