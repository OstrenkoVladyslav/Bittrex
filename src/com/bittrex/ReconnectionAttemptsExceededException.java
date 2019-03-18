package com.bittrex;

public class ReconnectionAttemptsExceededException extends RuntimeException {
    public ReconnectionAttemptsExceededException(String message) {
        super(message);
    }
}
