package com.simplificacontabil.dto;

import com.simplificacontabil.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long clienteId;
    private Long mesaId;
    private List<ItemPedidoDTO> itens;
    private boolean pago;
    private String observacao;
    private String nomeMotoboy;
    private BigDecimal trocoPara;
    private Boolean precisaTroco;
    private StatusPedido status;
    private EnderecoDTO endereco;
    private String tipoEntrega;
}
