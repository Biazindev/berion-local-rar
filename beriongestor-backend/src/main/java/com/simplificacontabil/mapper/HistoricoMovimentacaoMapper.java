package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.HistoricoMovimentacaoDTO;
import com.simplificacontabil.model.HistoricoMovimentacao;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HistoricoMovimentacaoMapper {
    HistoricoMovimentacaoDTO toDTO(HistoricoMovimentacao entity);
    HistoricoMovimentacao toEntity(HistoricoMovimentacaoDTO dto);
}
