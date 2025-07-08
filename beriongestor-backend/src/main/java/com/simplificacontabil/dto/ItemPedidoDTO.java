package com.simplificacontabil.dto;

import com.simplificacontabil.model.Produto;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDTO {
    @ManyToOne
    private Long produtoId;
    private Integer quantidade;
    private String observacao;
}

