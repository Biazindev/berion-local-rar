package com.simplificacontabil.exception;

public class FornecedorNotFoundException extends RuntimeException {
    public FornecedorNotFoundException(String message) {
        super(message);
    }
}