package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import lombok.*;
import com.fasterxml.jackson.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProdutoNfeDTO {

    @JsonProperty("cProd")
    private String cProd;

    @JsonProperty("cEAN")
    private String cEAN;

    @JsonProperty("cBarra")
    private String cBarra;

    @JsonProperty("xProd")
    private String xProd;

    @JsonProperty("NCM")
    private String NCM;

    @JsonProperty("NVE")
    private List<String> NVE;

    @JsonProperty("CEST")
    private String CEST;

    @JsonProperty("indEscala")
    private String indEscala;

    @JsonProperty("CNPJFab")
    private String CNPJFab;

    @JsonProperty("cBenef")
    private String cBenef;

    @JsonProperty("EXTIPI")
    private String EXTIPI;

    @JsonProperty("CFOP")
    private String CFOP;

    @JsonProperty("uCom")
    private String uCom;

    @JsonProperty("qCom")
    private BigDecimal qCom;

    @JsonProperty("vUnCom")
    private BigDecimal vUnCom;

    @JsonProperty("vProd")
    private BigDecimal vProd;

    @JsonProperty("cEANTrib")
    private String cEANTrib;

    @JsonProperty("cBarraTrib")
    private String cBarraTrib;

    @JsonProperty("uTrib")
    private String uTrib;

    @JsonProperty("qTrib")
    private BigDecimal qTrib;

    @JsonProperty("vUnTrib")
    private BigDecimal vUnTrib;

    @JsonProperty("vFrete")
    private BigDecimal vFrete;

    @JsonProperty("vSeg")
    private BigDecimal vSeg;

    @JsonProperty("vDesc")
    private BigDecimal vDesc;

    @JsonProperty("vOutro")
    private BigDecimal vOutro;

    @JsonProperty("indTot")
    private Integer indTot;

    @JsonProperty("xPed")
    private String xPed;

    @JsonProperty("nItemPed")
    private Integer nItemPed;
}
