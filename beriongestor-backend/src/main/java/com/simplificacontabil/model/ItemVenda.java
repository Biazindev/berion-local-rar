package com.simplificacontabil.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.simplificacontabil.util.MoedaUtil;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference  // Esta anotação previne que a venda seja serializada novamente dentro de ItemVenda
    @JoinColumn(name = "venda_id")
    private Venda venda;
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    private String nomeProduto;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal totalItem;
    @ManyToOne
    private NotaFiscal notaFiscal;
}
