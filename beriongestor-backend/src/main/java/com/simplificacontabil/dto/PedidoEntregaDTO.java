package com.simplificacontabil.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoEntregaDTO {

    private Long cliente_id; // será usado sempre que disponível

    private String fone; // backup pra buscar cliente por telefone, se cliente_id for nulo

    private EnderecoDTO enderecoEntrega;

    private boolean pago;
    private String observacao;

    private String nomeMotoboy;

    private List<ProdutoPedidoDTO> produtos;

    private String status; // EM_PREPARO, SAIU_PARA_ENTREGA, etc.
}
