package com.github.kalininaleksandrv.simpletracker.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.AbstractMap;

@ControllerAdvice
@Component
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler
    public ResponseEntity<AbstractMap.SimpleEntry<String, String>> handle(Exception exception) {
        LOG.error("Request could not be processed: ", exception);
        AbstractMap.SimpleEntry<String, String> response;

        if (exception instanceof IssueProcessingException ||
                exception instanceof DeveloperException ||
                exception instanceof HttpMessageNotReadableException) {
            response = new AbstractMap.SimpleEntry<>("message", exception.getMessage());
        } else {
            response = new AbstractMap.SimpleEntry<>("message", "Request could not be processed");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
