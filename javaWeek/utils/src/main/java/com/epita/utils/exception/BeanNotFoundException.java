package com.epita.utils.exception;

public class BeanNotFoundException extends RuntimeException {

    public BeanNotFoundException() {
        super(ExceptionMessages.BEAN_NOT_FOUND.message);
    }


}
