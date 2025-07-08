package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdeDTO {

    @JsonProperty("cUF")
    private Integer cUF;

    @JsonProperty("cNF")
    private String cNF;

    @JsonProperty("natOp")
    private String natOp;

    @JsonProperty("mod")
    private Integer mod;

    @JsonProperty("serie")
    private Integer serie;

    @JsonProperty("nNF")
    private Integer nNF;

    @JsonProperty("dhEmi")
    private String dhEmi;

    @JsonProperty("dhSaiEnt")
    private String dhSaiEnt;

    @JsonProperty("tpNF")
    private Integer tpNF;

    @JsonProperty("idDest")
    private Integer idDest;

    @JsonProperty("cMunFG")
    private String cMunFG;

    @JsonProperty("tpImp")
    private Integer tpImp;

    @JsonProperty("tpEmis")
    private Integer tpEmis;

    @JsonProperty("cDV")
    private Integer cDV;

    @JsonProperty("tpAmb")
    private Integer tpAmb;

    @JsonProperty("finNFe")
    private Integer finNFe;

    @JsonProperty("indFinal")
    private Integer indFinal;

    @JsonProperty("indPres")
    private Integer indPres;

    @JsonProperty("indIntermed")
    private Integer indIntermed;

    @JsonProperty("procEmi")
    private Integer procEmi;

    @JsonProperty("verProc")
    private String verProc;

    @JsonProperty("dhCont")
    private String dhCont;

    @JsonProperty("xJust")
    private String xJust;

    @JsonProperty("NFref")
    private List<NFrefDTO> NFref;
}
