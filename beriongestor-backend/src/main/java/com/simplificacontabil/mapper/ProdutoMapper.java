package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.ProdutoDTO;
import com.simplificacontabil.model.Produto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    ProdutoDTO toDTO(Produto produto);
    Produto toEntity(ProdutoDTO dto);
}
