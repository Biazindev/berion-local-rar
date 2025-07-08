package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.SimplesNacDTO;
import com.simplificacontabil.model.SimplesNac;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SimplesNacMapper {
    SimplesNac toEntity(SimplesNac simplesNac);
    SimplesNacDTO toDTO(SimplesNac simplesNac);
}
