// EnderecoMapper.java
package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.EnderecoDTO;
import com.simplificacontabil.model.Endereco;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {
    EnderecoDTO toDTO(Endereco endereco);
    Endereco toEntity(EnderecoDTO dto);
}
