package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.VendaDTO;
import com.simplificacontabil.enums.FormaPagamento;
import com.simplificacontabil.model.Venda;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface VendaMapper {
    VendaDTO toDTO(Venda venda);
    Venda toEntity(VendaDTO dto);
}
