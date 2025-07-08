package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.PedidoEntregaDTO;
import com.simplificacontabil.dto.ProdutoDTO;
import com.simplificacontabil.model.ItemPedido;
import com.simplificacontabil.model.Pedido;
import org.mapstruct.Mapper;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PedidoMapper {
    PedidoEntregaDTO toEntregaDTO(Pedido pedido);
    Pedido toEntity(PedidoEntregaDTO dto);
}
