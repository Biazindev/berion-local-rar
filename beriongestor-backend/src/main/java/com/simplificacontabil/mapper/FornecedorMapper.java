package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.FornecedorDTO;
import com.simplificacontabil.model.Fornecedor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { PessoaFisicaMapper.class, PessoaJuridicaMapper.class })
public interface FornecedorMapper {
    Fornecedor toEntity(FornecedorDTO dto);
    FornecedorDTO toDTO(Fornecedor entity);
}