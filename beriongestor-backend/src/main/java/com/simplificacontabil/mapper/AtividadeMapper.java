package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.AtividadeDTO;
import com.simplificacontabil.model.Atividade;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AtividadeMapper {
    Atividade toEntity(AtividadeDTO dto);
    AtividadeDTO toDTO(Atividade entity);
}
