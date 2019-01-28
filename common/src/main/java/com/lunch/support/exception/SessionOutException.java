package com.lunch.support.exception;

public class SessionOutException extends RuntimeException {
    public SessionOutException() {
        super();
    }

    public SessionOutException(String msg) {
        super(msg);
    }
}
