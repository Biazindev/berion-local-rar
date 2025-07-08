package com.simplificacontabil.dto;

import com.simplificacontabil.enums.StatusContaPagar;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaPagarDTO {

    private String  fornecedor;
    private String descricao;
    private BigDecimal valor;
    private LocalDate vencimento;
    private Boolean pago;
    private LocalDate dataPagamento;
    private StatusContaPagar status;
}
