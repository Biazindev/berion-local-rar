package com.simplificacontabil.dto;

import lombok.Data;

@Data
public class ProducaoRequest {
    private Long receitaId;
    private int quantidade;
}