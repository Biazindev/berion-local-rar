package com.simplificacontabil.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "pedido_entrega")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clienteId;

    private String fone;

    @Embedded
    private Endereco enderecoEntrega;

    private boolean pago;

    private String observacao;

    private String nomeMotoboy;

    private Boolean precisaTroco;

    private BigDecimal trocoPara;

    private String status;

    @OneToMany(mappedBy = "pedidoEntrega", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoPedido> produtos;
}

