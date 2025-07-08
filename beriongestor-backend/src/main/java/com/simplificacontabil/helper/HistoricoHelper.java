package com.simplificacontabil.helper;

import com.simplificacontabil.dto.HistoricoMovimentacaoDTO;
import com.simplificacontabil.enums.TipoMovimentacao;
import com.simplificacontabil.service.HistoricoMovimentacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class HistoricoHelper {

    private final HistoricoMovimentacaoService historicoService;

    public void registrar(TipoMovimentacao tipo, String descricao, String entidade, Long entidadeId, String usuario)
    {
        historicoService.registrar(HistoricoMovimentacaoDTO.builder()
                .tipoMovimentacao(tipo)
                .descricao(descricao)
                .entidade(entidade)
                .entidadeId(entidadeId)
                .usuarioResponsavel(usuario)
                .dataHora(LocalDateTime.now()) // define aqui caso o DTO não defina
                .build());
    }
}
