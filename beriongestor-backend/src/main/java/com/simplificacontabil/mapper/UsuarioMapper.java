package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.UsuarioDTO;
import com.simplificacontabil.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioDTO toDTO(Usuario usuario);
    Usuario toEntity(UsuarioDTO usuario);

}
