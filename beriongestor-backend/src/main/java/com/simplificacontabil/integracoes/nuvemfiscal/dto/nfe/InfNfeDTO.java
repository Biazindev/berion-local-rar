package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.simplificacontabil.dto.EmitenteDTO;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfNfeDTO {

    @JsonProperty("versao")
    private String versao;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("ide")
    private IdeDTO ide;

    @JsonProperty("emit")
    private EmitenteDTO emit;

    @JsonProperty("avulsa")
    private AvulsaDTO avulsa;

    @JsonProperty("dest")
    private DestinatarioDTO dest;

    @JsonProperty("retirada")
    private LocalEntregaRetiradaDTO retirada;

    @JsonProperty("entrega")
    private LocalEntregaRetiradaDTO entrega;

    @JsonProperty("autXML")
    private List<AutorizadoXmlDTO> autXML;

    @JsonProperty("det")
    private List<DetDTO> det;

    @JsonProperty("total")
    private TotalDTO total;

    @JsonProperty("transp")
    private TransporteDTO transp;

    @JsonProperty("cobr")
    private CobrancaDTO cobr;

    @JsonProperty("pag")
    private PagamentoNfeDTO pag;

    @JsonProperty("infIntermed")
    private IntermediadorDTO infIntermed;

    @JsonProperty("infAdic")
    private InformacoesAdicionaisDTO infAdic;

    @JsonProperty("exporta")
    private ExportacaoDTO exporta;

    @JsonProperty("compra")
    private CompraDTO compra;

    @JsonProperty("cana")
    private CanaDTO cana;

    @JsonProperty("infRespTec")
    private ResponsavelTecnicoDTO infRespTec;

    @JsonProperty("infSolicNFF")
    private SolicitacaoNFFDTO infSolicNFF;

    @JsonProperty("agropecuario")
    private AgropecuarioDTO agropecuario;
}
