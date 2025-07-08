package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.ContaPagarDTO;
import com.simplificacontabil.model.ContaPagar;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ContaPagarMapper {

    ContaPagarDTO toDTO(ContaPagar conta);

    ContaPagar toEntity(ContaPagarDTO dto);
}
