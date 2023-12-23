package org.example.exceptions;

public class NotAccessException extends RuntimeException {

    String message;

    public NotAccessException(String message) {

        this.message = message;
    }
}
