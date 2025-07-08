package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.ClienteDTO;
import com.simplificacontabil.model.Cliente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = { PessoaFisicaMapper.class,
                PessoaJuridicaMapper.class })
public interface ClienteMapper {
    ClienteDTO toDTO(Cliente entidade);
    Cliente toEntity(ClienteDTO dto);
}
