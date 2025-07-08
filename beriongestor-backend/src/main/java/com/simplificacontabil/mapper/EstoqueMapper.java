package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.EstoqueDTO;
import com.simplificacontabil.model.Estoque;
import com.simplificacontabil.model.Produto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstoqueMapper {
    Estoque toEntity(EstoqueDTO dto);
    EstoqueDTO toDTO(Estoque entity);
}
