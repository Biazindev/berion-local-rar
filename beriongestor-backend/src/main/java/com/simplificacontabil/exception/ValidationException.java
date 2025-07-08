package com.simplificacontabil.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {
    public ValidationException() {
        super("Operação inválida");
    }
    public ValidationException(String message) {
        super(message);
    }
}
