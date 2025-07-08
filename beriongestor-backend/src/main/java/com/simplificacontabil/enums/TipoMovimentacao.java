package com.simplificacontabil.enums;

import lombok.Getter;

@Getter
public enum TipoMovimentacao {

    ESTOQUE("Estoque"),
    FINANCEIRO("Financeiro"),
    SERVICO("Serviço"),
    ENTRADA("Entrada"),
    SAIDA("Saída");

    private final String descricao;

    TipoMovimentacao(String descricao) {
        this.descricao = descricao;
    }
}
