package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import lombok.*;
import com.fasterxml.jackson.annotation.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AvulsaDTO {
    private String CNPJ;
    private String xOrgao;
    private String matr;
    private String xAgente;
    private String fone;
    private String UF;
    private String nDAR;
    private String dEmi;
    private Double vDAR;
    private String repEmi;
    private String dPag;
}