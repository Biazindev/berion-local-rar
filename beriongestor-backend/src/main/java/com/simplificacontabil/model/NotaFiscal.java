package com.simplificacontabil.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaFiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numero;
    private String serie;
    private LocalDateTime dataEmissao;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    private BigDecimal valorTotal;
    private String descricao;
    private String tipo; // Entrada ou Saída
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nota_fiscal_id") // cria a FK no ItemVenda
    private List<ItemVenda> itens; // ou outro nome que represente os produtos
    private BigDecimal totalImpostos;

}
