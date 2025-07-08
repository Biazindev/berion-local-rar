package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public  class ServicoDTO {
    private String descricao;
    private Double valor;
    private String codigoTributacaoMunicipal;   // cTribMun
    private String codigoTributacaoNacional;    // cTribNac
    private String cnae;
    private String nbs;
    private String informacoesComplementares;
    private LocalPrestacao locPrest; // ✅ novo campo
    @JsonProperty("cServ")
    private CodigoServico cServ; // ✅ bloco com os códigos fiscais
    private InfoComplDTO infoCompl;

}



