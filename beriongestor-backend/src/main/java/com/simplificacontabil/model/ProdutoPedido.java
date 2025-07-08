package com.simplificacontabil.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "produto_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long produtoId;

    private String nomeProduto;

    private Integer quantidade;

    private BigDecimal precoUnitario;

    private BigDecimal totalItem;

    @ManyToOne
    @JoinColumn(name = "pedido_entrega_id")
    private PedidoEntrega pedidoEntrega;
}
