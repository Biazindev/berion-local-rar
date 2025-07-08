package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigRegTribDTO {
    private Integer opSimpNac;
    private Integer regApTribSN;
    private Integer regEspTrib;
}
