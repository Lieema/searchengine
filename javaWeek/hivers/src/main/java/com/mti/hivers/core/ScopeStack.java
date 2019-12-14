package com.mti.hivers.core;

import com.epita.utils.annotation.Mutate;
import com.epita.utils.annotation.NotNull;
import com.epita.utils.annotation.Pure;
import com.epita.utils.exception.InvalidScopePopException;

import java.util.Deque;
import java.util.Optional;

import static com.epita.utils.validation.Fault.NULL;

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
