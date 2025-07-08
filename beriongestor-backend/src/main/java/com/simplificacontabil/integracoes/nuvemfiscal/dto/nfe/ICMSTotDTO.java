package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import lombok.*;
import com.fasterxml.jackson.annotation.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ICMSTotDTO {

    @JsonProperty("vBC")
    private BigDecimal vBC;

    @JsonProperty("vICMS")
    private BigDecimal vICMS;

    @JsonProperty("vICMSDeson")
    private BigDecimal vICMSDeson;

    @JsonProperty("vFCPUFDest")
    private BigDecimal vFCPUFDest;

    @JsonProperty("vICMSUFDest")
    private BigDecimal vICMSUFDest;

    @JsonProperty("vICMSUFRemet")
    private BigDecimal vICMSUFRemet;

    @JsonProperty("vFCP")
    private BigDecimal vFCP;

    @JsonProperty("vBCST")
    private BigDecimal vBCST;

    @JsonProperty("vST")
    private BigDecimal vST;

    @JsonProperty("vFCPST")
    private BigDecimal vFCPST;

    @JsonProperty("vFCPSTRet")
    private BigDecimal vFCPSTRet;

    @JsonProperty("vProd")
    private BigDecimal vProd;

    @JsonProperty("vFrete")
    private BigDecimal vFrete;

    @JsonProperty("vSeg")
    private BigDecimal vSeg;

    @JsonProperty("vDesc")
    private BigDecimal vDesc;

    @JsonProperty("vII")
    private BigDecimal vII;

    @JsonProperty("vIPI")
    private BigDecimal vIPI;

    @JsonProperty("vIPIDevol")
    private BigDecimal vIPIDevol;

    @JsonProperty("vPIS")
    private BigDecimal vPIS;

    @JsonProperty("vCOFINS")
    private BigDecimal vCOFINS;

    @JsonProperty("vOutro")
    private BigDecimal vOutro;

    @JsonProperty("vNF")
    private BigDecimal vNF;

    @JsonProperty("vTotTrib")
    private BigDecimal vTotTrib;
}
