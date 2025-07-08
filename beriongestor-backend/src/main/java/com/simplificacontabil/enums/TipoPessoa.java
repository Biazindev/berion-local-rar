package com.simplificacontabil.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoPessoa {

    JURIDICA,
    FISICA;

    @JsonCreator
    public static TipoPessoa from(String value) {
        return switch (value.toUpperCase()) {
            case "FISICA", "PF" ,"FÍSICA" -> FISICA;
            case "JURIDICA", "PJ", "JURÍDICA" -> JURIDICA;
            default -> throw new IllegalArgumentException("TipoPessoa inválido: " + value);
        };
    }
}
