package com.simplificacontabil.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.simplificacontabil.enums.StatusOrdemServico;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdemServicoDTO {

    private Long id;
    private Long clienteId;
    private String descricao;
    private LocalDate dataAbertura;
    private LocalDate dataConclusao;
    private StatusOrdemServico status;
    private BigDecimal valor;
}
