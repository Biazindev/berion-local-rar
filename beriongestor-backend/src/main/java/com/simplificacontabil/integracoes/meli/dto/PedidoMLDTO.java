package com.simplificacontabil.integracoes.meli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoMLDTO {
    private String id;
    private String status;
    private LocalDateTime dataCriacao;
    private BigDecimal valorTotal;
    private String nomeComprador;
}
