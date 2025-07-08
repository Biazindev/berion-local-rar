package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.HistoricoMovimentacaoDTO;
import com.simplificacontabil.dto.MovimentacaoDTO;
import com.simplificacontabil.model.HistoricoMovimentacao;
import com.simplificacontabil.model.MovimentacaoEstoque;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MovimentacaoMapper {
    HistoricoMovimentacaoDTO toDTO(HistoricoMovimentacao entity);

    HistoricoMovimentacao toEntity(HistoricoMovimentacaoDTO dto);

    // Movimentação estoque
    MovimentacaoDTO toDTO(MovimentacaoEstoque entity);
    List<MovimentacaoDTO> toDtoList(List<MovimentacaoEstoque> movs);
}
