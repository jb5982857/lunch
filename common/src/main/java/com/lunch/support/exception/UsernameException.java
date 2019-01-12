package com.lunch.support.exception;

public class UsernameException extends RuntimeException {
    public UsernameException() {
        super();
    }

    public UsernameException(String message) {
        super(message);
    }
}
