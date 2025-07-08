package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.ProdutoPedidoDTO;
import com.simplificacontabil.model.ProdutoPedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProdutoPedidoMapper {

    @Mapping(target = "pedidoEntrega", ignore = true)
    ProdutoPedido toEntity(ProdutoPedidoDTO dto);

    ProdutoPedidoDTO toDto(ProdutoPedido entity);

    @Mapping(target = "pedidoEntrega", ignore = true)
    void updateEntityFromDto(ProdutoPedidoDTO dto, @MappingTarget ProdutoPedido entity);
}
