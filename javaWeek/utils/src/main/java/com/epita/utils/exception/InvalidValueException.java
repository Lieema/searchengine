package com.epita.utils.exception;

import com.epita.utils.validation.Fault;

public class InvalidValueException extends RuntimeException
{
    public final Fault fault;

    public InvalidValueException(final Fault fault) {
        this.fault = fault;
    }

    @Override
    public String getMessage() {
        return "Invalid value, " + fault.message;
    }


}
