package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.CategoriaDTO;
import com.simplificacontabil.model.Categoria;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    Categoria toEntity(CategoriaDTO dto);
    CategoriaDTO toDTO(Categoria entity);
}
