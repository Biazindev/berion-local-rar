package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntermediarioDTO {
    private String CNPJ;
    private String CPF;
    private String NIF;
    private Integer cNaoNIF;
    private String CAEPF;
    private String IM;
    private String IE;
    private String xNome;
    private EnderecoDTO end;
    private String fone;
    private String email;
}
