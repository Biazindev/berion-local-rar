package com.simplificacontabil.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.simplificacontabil.enums.StatusPedido;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonBackReference
    @Nullable
    private Mesa mesa;
    private LocalDateTime horaPedido;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ItemPedido> itens;
    private boolean pago;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;
    private String observacao;
    private String nomeMotoboy;
    private Boolean precisaTroco;
    private BigDecimal trocoPara;
    @Embedded
    private Endereco enderecoEntrega;
    @ManyToOne
    @JoinColumn(
            name = "cliente_id",
            foreignKey = @ForeignKey(
                    name = "fk_cliente",
                    foreignKeyDefinition = "FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE"
            )
    )
    @JsonIgnore
    private Cliente cliente;
}
