package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigRegimeTributarioDTO {
    private int opSimpNac;
    private int regApTribSN;
    private int regEspTrib;
}
