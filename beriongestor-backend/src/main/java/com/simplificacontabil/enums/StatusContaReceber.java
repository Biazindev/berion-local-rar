package com.simplificacontabil.enums;

public enum StatusContaReceber {


    PENDENTE("Pendente"),
    PAGO("Pago"),
    ATRASADO("Atrasado");

    private final String descricao;

    StatusContaReceber(String descricao) {
        this.descricao = descricao;
    }
}
