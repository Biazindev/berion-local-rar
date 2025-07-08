package com.simplificacontabil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoMesaDTO {
    private Integer numeroMesa;
    private List<ItemPedidoDTO> itens;
}