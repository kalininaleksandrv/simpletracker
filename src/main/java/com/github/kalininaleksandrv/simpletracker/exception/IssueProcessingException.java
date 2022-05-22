package com.github.kalininaleksandrv.simpletracker.exception;

public class IssueProcessingException extends RuntimeException{

    public IssueProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
    public IssueProcessingException(String message) {
        super(message);
    }
}
