package com.epita.utils.exception;

public enum ExceptionMessages {
    BEAN_NOT_FOUND("No bean of the given type has been found."),
    PROXY_TYPE_NOT_AN_INTERFACE("Binding proxy type is not an interface."),
    INVALID_SCOPE_POP("Tried to pop a scope, but not enough left on the stack.");

    public final String message;
    ExceptionMessages(final String message) {
        this.message = message;
    }
}
