package com.simplificacontabil.model;

import com.simplificacontabil.enums.StatusContaPagar;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaPagar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String  fornecedor;
    private String descricao;
    private BigDecimal valor;
    private LocalDate vencimento;
    private Boolean pago;
    private LocalDate dataPagamento;
    private StatusContaPagar status;
}
