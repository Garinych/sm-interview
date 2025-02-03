package com.dmitrii_sandul.sm.exception;

public class NoCopiesAvailableException extends RuntimeException {
    public NoCopiesAvailableException(String message) {
        super(message);
    }
}