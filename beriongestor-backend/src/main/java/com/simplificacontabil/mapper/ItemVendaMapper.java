package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.ItemVendaDTO;
import com.simplificacontabil.model.ItemVenda;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemVendaMapper {

    ItemVendaDTO toDTO(ItemVenda itemVenda);
    ItemVenda toEntity(ItemVendaDTO dto);
    List<ItemVendaDTO> toDTOList(List<ItemVenda> itemVendas);
    List<ItemVenda> toEntityList(List<ItemVendaDTO> dtos);
}
