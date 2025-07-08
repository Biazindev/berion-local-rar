package com.simplificacontabil.enums;

import lombok.Getter;

@Getter
public enum StatusContaPagar {
    A_VENCER("A vencer"),
    VENCIDA("Vencida"),
    PENDENTE("Pendente"),
    PAGO("Pago"),
    ATRASADO("Atrasado");

    private final String descricao;

    StatusContaPagar(String descricao) {
        this.descricao = descricao;
    }
}