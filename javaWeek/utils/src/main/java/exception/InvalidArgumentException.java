package exception;

import validation.Fault;

public class InvalidArgumentException extends RuntimeException
{

    public final String field;
    public final Fault fault;

    public InvalidArgumentException(final String field, final Fault fault) {
        this.field = field;
        this.fault = fault;
    }

    @Override
    public String getMessage() {
        return "Invalid argument : " + field + " " + fault.message;
    }


}
