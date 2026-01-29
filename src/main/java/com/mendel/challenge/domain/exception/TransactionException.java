package com.mendel.challenge.domain.exception;

public class TransactionException extends RuntimeException {
    public TransactionException(String message) {
        super(message);
    }
}
