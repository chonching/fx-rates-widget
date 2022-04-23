package com.example.exam.exception;

public class CurrencyNotFoundException extends RuntimeException {

    public CurrencyNotFoundException() {
        super();
    }

    public CurrencyNotFoundException(String message) {
        super(message);
    }
}
