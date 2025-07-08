package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigPrefeituraDTO {
    private String login;
    private String senha;
    private String token;
}
