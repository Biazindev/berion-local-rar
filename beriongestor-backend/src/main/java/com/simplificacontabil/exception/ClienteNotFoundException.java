package com.simplificacontabil.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Lançada quando um Cliente não é encontrado no banco.
 * Devolve HTTP 404.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClienteNotFoundException extends RuntimeException {
    public ClienteNotFoundException() {
        super("Cliente não encontrado");
    }
    public ClienteNotFoundException(String message) {
        super(message);
    }
}
