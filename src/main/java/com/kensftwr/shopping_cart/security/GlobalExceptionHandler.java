package com.kensftwr.shopping_cart.security;

import com.kensftwr.shopping_cart.dtos.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> loginFail(@RequestBody LoginRequest loginRequest) {
        String message = "You do not have permission to access this resource";
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }
}
