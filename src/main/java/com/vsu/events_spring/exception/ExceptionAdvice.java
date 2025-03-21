package com.vsu.events_spring.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getFieldErrors().stream().map(it -> it.getField() + " " + it.getDefaultMessage())
                .collect(Collectors.toList());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = {IOException.class})
    public ResponseEntity<Object> handleIOException(IOException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed");
    }


    @ExceptionHandler(value = {ProfileNotFountException.class, EventNotFountException.class, LightRoomNotFountException.class})
    public ResponseEntity<Object> handleEntityNotFountException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found: " + ex.getMessage());
    }

    @ExceptionHandler(value = {UnauthorizedAccessException.class})
    public ResponseEntity<Object> UnauthorizedAccessException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No access: " + ex.getMessage());
    }

    @ExceptionHandler(value = {LoginExistsException.class})
    public ResponseEntity<Object> LLoginExistsException(Exception ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("A user with this login already exists: " + ex.getMessage());
    }
}
