package com.simplificacontabil.enums;

import lombok.Getter;

@Getter
public enum StatusOrdemServico {

    ABERTA("Aberta"),
    EM_ANDAMENTO("Em andamento"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada");

    private final String descricao;

    StatusOrdemServico(String descricao) {
        this.descricao = descricao;
    }
}
