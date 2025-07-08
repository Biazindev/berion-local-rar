package com.simplificacontabil.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simplificacontabil.enums.TipoMovimentacao;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoMovimentacaoDTO {
    private Long id;
    private TipoMovimentacao tipoMovimentacao;
    private String descricao;
    private String entidade;
    private Long entidadeId;
    private LocalDateTime dataHora;
    private String usuarioResponsavel;

    @JsonProperty("descricaoTipo")
    public String getDescricaoTipo() {
        return tipoMovimentacao != null ? tipoMovimentacao.getDescricao() : null;
    }

}
