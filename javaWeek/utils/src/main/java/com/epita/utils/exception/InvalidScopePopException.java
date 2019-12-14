package com.epita.utils.exception;

public class InvalidScopePopException extends RuntimeException {

    public InvalidScopePopException() {
        super(ExceptionMessages.INVALID_SCOPE_POP.message);
    }
}
