package com.simplificacontabil.model;

import com.simplificacontabil.enums.FormaPagamento;
import com.simplificacontabil.util.MoedaUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private FormaPagamento formaPagamento;
    private BigDecimal valorPago;

    @ManyToOne
    @JoinColumn(name = "venda_id")
    private Venda venda;

    private BigDecimal valorRestante;
    private String dataPagamento;
    private String status;
    private int numeroParcelas;
    private BigDecimal totalVenda;
    private BigDecimal totalDesconto;
    private BigDecimal totalPagamento;
}
