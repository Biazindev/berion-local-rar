package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.EmpresaDTO;
import com.simplificacontabil.model.Empresa;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmpresaMapper {
    EmpresaMapper INSTANCE = Mappers.getMapper(EmpresaMapper.class);

    EmpresaDTO toDto(Empresa empresa);
    Empresa toEntity(EmpresaDTO empresaDTO);

    List<EmpresaDTO> toDtoList(List<Empresa> empresas);
    List<Empresa> toEntityList(List<EmpresaDTO> empresaDTOs);
}
