package com.simplificacontabil.dto;

import com.simplificacontabil.enums.FormaPagamento;
import com.simplificacontabil.util.MoedaUtil;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoDTO {

    private Long id;
    private Long vendaId; // Referência à venda relacionada, não duplicar os dados da venda.
    private FormaPagamento formaPagamento;  // Forma de pagamento
    private String valorPago;
    private BigDecimal valorRestante;  // Valor restante (se houver)
    private String dataPagamento;  // Data do pagamento
    private String status;  // Status do pagamento ("Pago", "Pendente", etc.)
    private int numeroParcelas; // Número de parcelas, se for o caso
    private BigDecimal totalVenda;
    private BigDecimal totalDesconto;
    private BigDecimal totalPagamento;
}
