package com.dmitrii_sandul.sm.exception;

public class BorrowRecordNotFoundException extends RuntimeException {
    public BorrowRecordNotFoundException(String message) {
        super(message);
    }
}