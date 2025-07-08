// PessoaJuridicaMapper.java
package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.PessoaJuridicaDTO;
import com.simplificacontabil.model.PessoaJuridica;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { AtividadeMapper.class, SocioMapper.class })
public abstract class PessoaJuridicaMapper {

    public abstract PessoaJuridica toEntity(PessoaJuridicaDTO dto);

    public abstract PessoaJuridicaDTO toDTO(PessoaJuridica entity);

    @AfterMapping
    protected void setRelacionamentos(@MappingTarget PessoaJuridica entity) {
        if (entity.getAtividadesPrincipais() != null) {
            entity.getAtividadesPrincipais().forEach(a -> a.setPessoaJuridica(entity));
        }
        if (entity.getAtividadesSecundarias() != null) {
            entity.getAtividadesSecundarias().forEach(a -> a.setPessoaJuridica(entity));
        }
        if (entity.getSocios() != null) {
            entity.getSocios().forEach(s -> s.setPessoaJuridica(entity));
        }
    }
}
