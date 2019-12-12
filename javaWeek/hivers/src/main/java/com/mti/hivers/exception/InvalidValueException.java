package com.mti.hivers.exception;

import com.mti.hivers.validation.Fault;

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
