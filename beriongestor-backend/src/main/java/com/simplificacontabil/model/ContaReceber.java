package com.simplificacontabil.model;

import com.simplificacontabil.enums.StatusContaPagar;
import com.simplificacontabil.enums.StatusContaReceber;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaReceber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cliente;

    private String descricao;
    private BigDecimal valor;
    private LocalDate vencimento;
    private Boolean recebido;
    private LocalDate dataRecebimento;
    private StatusContaReceber status;
}
