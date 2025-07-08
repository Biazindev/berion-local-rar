package com.simplificacontabil.dto;

import com.simplificacontabil.enums.StatusContaPagar;
import com.simplificacontabil.enums.StatusContaReceber;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaReceberDTO {
    private Long id;

    private String cliente;
    private String descricao;
    private BigDecimal valor;
    private LocalDate vencimento;
    private Boolean recebido;
    private LocalDate dataRecebimento;
    private StatusContaReceber status;

}
