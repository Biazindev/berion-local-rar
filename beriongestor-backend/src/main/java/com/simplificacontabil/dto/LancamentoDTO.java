package com.simplificacontabil.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LancamentoDTO {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private Long clienteId;
}
