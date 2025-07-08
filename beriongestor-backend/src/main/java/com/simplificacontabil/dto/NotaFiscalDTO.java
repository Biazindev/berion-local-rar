package com.simplificacontabil.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaFiscalDTO {
    private Long id;
    private String numero;
    private String serie;
    private LocalDateTime dataEmissao;
    private Long clienteId;
    private String nomeCliente; // opcional
    private BigDecimal valorTotal;
    private String descricao;
    private String tipo;
}
