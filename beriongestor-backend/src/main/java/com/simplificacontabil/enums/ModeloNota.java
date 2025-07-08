package com.simplificacontabil.enums;

public enum ModeloNota {
    NFE("55"),
    NFCE("65"),
    NFSE("55");

    private final String codigo;

    ModeloNota(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
