package com.eventorganizer.event_org;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    // This handles IllegalArgumentException everywhere in your app
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()   // HTTP 400
                .body(ex.getMessage());  // The message you pass when throwing exception
    }

}
