package com.simplificacontabil.mapper;


import com.simplificacontabil.dto.PedidoEntregaDTO;
import com.simplificacontabil.model.PedidoEntrega;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        EnderecoMapper.class,
        ProdutoPedidoMapper.class
})
public interface PedidoEntregaMapper {

    @Mapping(target = "produtos", source = "produtos")
    PedidoEntrega toEntity(PedidoEntregaDTO dto);

    @Mapping(target = "produtos", source = "produtos")
    PedidoEntregaDTO toDto(PedidoEntrega entity);
}
