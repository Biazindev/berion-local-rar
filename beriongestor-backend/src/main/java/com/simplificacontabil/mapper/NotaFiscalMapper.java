package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.NotaFiscalDTO;
import com.simplificacontabil.model.Cliente;
import com.simplificacontabil.model.NotaFiscal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotaFiscalMapper {
    NotaFiscalDTO toDTO(NotaFiscal nf);
    NotaFiscal toEntity(NotaFiscalDTO dto);
}
