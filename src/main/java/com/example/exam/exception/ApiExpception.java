package com.example.exam.exception;

public class ApiExpception extends RuntimeException {
    public ApiExpception() {
        super();
    }

    public ApiExpception(String message) {
        super(message);
    }
}
