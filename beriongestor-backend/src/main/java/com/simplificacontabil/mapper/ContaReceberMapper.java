package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.ContaReceberDTO;
import com.simplificacontabil.model.Cliente;
import com.simplificacontabil.model.ContaReceber;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContaReceberMapper {

    ContaReceberDTO toDTO(ContaReceber conta);
    ContaReceber toEntity(ContaReceberDTO dto);

}
