package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import lombok.*;
import com.fasterxml.jackson.annotation.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocalEntregaRetiradaDTO {
    private String CNPJ;
    private String CPF;
    private String xNome;
    private String xLgr;
    private String nro;
    private String xCpl;
    private String xBairro;
    private String cMun;
    private String xMun;
    private String UF;
    private String CEP;
    private String cPais;
    private String xPais;
    private String fone;
    private String email;
    private String IE;
}