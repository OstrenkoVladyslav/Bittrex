package com.bittrex;

public class OperationIsFault extends RuntimeException {
    public OperationIsFault(String message) {
        super(message);
    }
}