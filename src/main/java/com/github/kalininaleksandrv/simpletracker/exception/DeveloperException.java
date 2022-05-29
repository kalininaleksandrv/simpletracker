package com.github.kalininaleksandrv.simpletracker.exception;

public class DeveloperException extends RuntimeException{

    public DeveloperException(String message, Throwable cause) {
        super(message, cause);
    }
    public DeveloperException(String message) {
        super(message);
    }
}
