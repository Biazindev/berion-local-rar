// PessoaFisicaMapper.java
package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.PessoaFisicaDTO;
import com.simplificacontabil.model.PessoaFisica;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { EnderecoMapper.class })
public interface PessoaFisicaMapper {

    @Mapping(source = "endereco", target = "endereco")
    PessoaFisicaDTO toDTO(PessoaFisica entidade);

    @Mapping(source = "endereco", target = "endereco")
    PessoaFisica toEntity(PessoaFisicaDTO dto);
}
