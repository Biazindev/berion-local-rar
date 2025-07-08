package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValorDeducoesDTO {
    private Double pDR;
    private Double vDR;
    private DocumentosDeducaoDTO documentos;
}
