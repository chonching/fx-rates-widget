package com.example.exam.exception;

public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException() {
        super();
    }

    public InvalidRequestException(String message) {
        super(message);
    }
}
