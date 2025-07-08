package com.simplificacontabil.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagamentoTefDTO {
    private BigDecimal valor;
    private String formaPagamento;
    private int numeroCaixa;       // identificador do caixa
}
