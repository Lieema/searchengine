package com.epita.utils.exception;

public class ProxyTypeNotAnInterfaceException extends RuntimeException {

    public ProxyTypeNotAnInterfaceException() {
        super(ExceptionMessages.PROXY_TYPE_NOT_AN_INTERFACE.message);
    }
}
