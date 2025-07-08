package com.simplificacontabil.dto;

import com.simplificacontabil.enums.TipoMovimentacao;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoDTO {
    private Long id;
    private Long produtoId;
    private String produtoNome;
    private LocalDateTime dataMovimentacao;
    private TipoMovimentacao tipo;
    private Integer quantidade;
    private String descricao;
}
