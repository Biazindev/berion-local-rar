package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigRpsDTO {
    private Integer lote;
    private String serie;
    private Integer numero;
}
