package com.dmitrii_sandul.sm.exception;

public class RetryException extends RuntimeException {
    public RetryException(String message) {
        super(message);
    }
}