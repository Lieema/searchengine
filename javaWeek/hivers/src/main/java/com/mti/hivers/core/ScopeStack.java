package com.mti.hivers.core;

import com.mti.hivers.annotation.Mutate;
import com.mti.hivers.annotation.NotNull;
import com.mti.hivers.annotation.Pure;
import com.mti.hivers.exception.InvalidArgumentException;
import com.mti.hivers.exception.InvalidScopePopException;

import java.util.Deque;
import java.util.Optional;

import static com.mti.hivers.validation.Fault.NULL;

@Mutate
public interface ScopeStack {

    int MIN_STACK_SIZE = 1;

    @NotNull
    Deque<Scope> getScopeStack();

    default void push(@NotNull final Scope scope) {
        NULL.validate(scope, "scope");
        getScopeStack().push(scope);
    }

    default void pop() {
        if (getScopeStack().size() <= MIN_STACK_SIZE) {
            throw new InvalidScopePopException();
        }
        getScopeStack().pop();
    }

    @Pure
    @NotNull
    default Scope peek() {
        return getScopeStack().peek();
    }

    @Pure
    @NotNull
    default <BEAN_TYPE> Optional<Provider<BEAN_TYPE>> getProviderForClass(@NotNull final Class<BEAN_TYPE> beanTypeClass) {

        NULL.validate(beanTypeClass, "beanTypeClass");

        return getScopeStack().stream()
                .map(scope -> scope.getProviderForClass(beanTypeClass))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}
