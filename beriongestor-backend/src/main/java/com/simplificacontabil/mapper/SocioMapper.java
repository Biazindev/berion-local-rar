package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.SocioDTO;
import com.simplificacontabil.model.Socio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SocioMapper {
    Socio toEntity(SocioDTO dto);
    SocioDTO toDTO(Socio entity);
}
