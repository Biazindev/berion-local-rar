package com.simplificacontabil.integracoes.nuvemfiscal.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplificacontabil.integracoes.nuvemfiscal.auth.NuvemFiscalAuthService;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse.EmitirNfseDpsRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NuvemFiscalNfseService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final NuvemFiscalAuthService authService;

    private static final String URL = "https://api.nuvemfiscal.com.br/nfse/dps";

//    public EmitirNfseDpsRequest montarPayloadNfseTeste() {
//        return EmitirNfseDpsRequest.builder()
//                .provedor("padrao")
//                .ambiente("homologacao")
//                .referencia("DPS-0001")
//                .infDPS(InfDpsDTO.builder()
//                        .tpAmb(2)
//                        .dhEmi(Instant.now().toString())
//                        .verAplic("1.0")
//                        .dCompet(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE))
//
//                        .prest(PrestadorDTO.builder()
//                                .CNPJ("47397316000122")
//                                .regTrib(RegimeTributario.builder().regEspTrib(1).build())
//                                .build())
//
//                        .toma(TomadorDTO.builder()
//                                .orgaoPublico(false)
//                                .CPF("06548386906")
//                                .IM("ISENTO")
//                                .IE("ISENTO")
//                                .xNome("Tiago Gofredo Biazin")
//                                .fone("17981716648")
//                                .email("tiago.biazin02@gmail.com")
//                                .end(EnderecoCompleto.builder()
//                                        .xLgr("Belluno")
//                                        .tpLgr("Rua")
//                                        .nro("50")
//                                        .xBairro("Tartarelli")
//                                        .endNac(EnderecoNacional.builder()
//                                                .cMun("4127205")
//                                                .CEP("79000327")
//                                                .build())
//                                        .endExt(EnderecoExterior.builder()
//                                                .cpais("BR")
//                                                .cEndPost("79000-327")
//                                                .xCidade("Terra Boa")
//                                                .xEstProvReg("PR")
//                                                .build())
//                                        .build())
//                                .build())
//
//                        .serv(ServicoDTO.builder()
//                                // local de prestação
//                                .locPrest(LocalPrestacao.builder()
//                                        .cLocPrestacao("4127205")
//                                        .cPaisPrestacao("BR")
//                                        .build())
//                                // código de serviço
//                                .cServ(CodigoServico.builder()
//                                        .cTribNac("1.05")
//                                        .cTribMun("6209100")
//                                        .CNAE("6201500")
//                                        .xDescServ("Suporte de TI")
//                                        .cNBS("123456000")
//                                        .build())
//                                // valores e retenções
//                                .valores(ValoresDeclaracaoServicoDTO.builder()
//                                        .valorServicos(250.00)
//                                        .valorDeducoes(0.00)
//                                        .aliquotaPis(0.00)
//                                        .retidoPis(0)
//                                        .valorPis(0.00)
//                                        .aliquotaCofins(0.00)
//                                        .retidoCofins(0)
//                                        .valorCofins(0.00)
//                                        .aliquotaInss(0.00)
//                                        .retidoInss(0)
//                                        .valorInss(0.00)
//                                        .aliquotaIr(0.00)
//                                        .retidoIr(0)
//                                        .valorIr(0.00)
//                                        .aliquotaCsll(0.00)
//                                        .retidoCsll(0)
//                                        .valorCsll(0.00)
//                                        .aliquotaCpp(0.00)
//                                        .retidoCpp(0)
//                                        .valorCpp(0.00)
//                                        .outrasRetencoes(0.00)
//                                        .retidoOutrasRetencoes(0)
//                                        .aliquotaTotTributos(0.00)
//                                        .valTotTributos(0.00)
//                                        .valorIss(5.00)
//                                        .aliquota(0.02)
//                                        .descontoIncondicionado(0.00)
//                                        .descontoCondicionado(0.00)
//                                        .build())
//                                .issRetido(1)
//                                .responsavelRetencao(1)
//                                .discriminacao("Suporte de TI prestado em maio/2025")
//                                .codigoTributacaoMunicipio("6209100")
//                                .codigoNbs("123456000")
//                                .codigoMunicipio(4127205)
//                                .codigoPais("1058")
//                                .exigibilidadeIss(1)
//                                // <-- municipioIncidencia antes de listaItensServico
//                                .municipioIncidencia(4127205)
//                                .numeroProcesso(null)
//                                // lista de itens de serviço
//                                .listaItensServico(ListaItensServicoDTO.builder()
//                                        .itemServico(Arrays.asList(
//                                                ItemServicoDTO.builder()
//                                                        .itemListaServico("1.02")
//                                                        .codigoCnae(6201500)
//                                                        .descricao("Manutenção de sistemas")
//                                                        .unidade("UN")
//                                                        .tributavel(1)
//                                                        .quantidade(new BigDecimal("1.00000"))
//                                                        .valorUnitario(new BigDecimal("250.00000"))
//                                                        .valorDesconto(new BigDecimal("0.00"))
//                                                        .valorLiquido(new BigDecimal("250.00"))
//                                                        .build()
//                                        ))
//                                        .build())
//                                .infoCompl(InfoComplDTO.builder()
//                                        .xInfComp("Emitido via Berion Gestor - Biazin Sistemas")
//                                        .build())
//                                .build())
//
//                        .valores(ValoresDTO.builder()
//                                .vServPrest(ValorServPrestDTO.builder().vServ(250.00).vReceb(250.00).build())
//                                .vDescCondIncond(ValorDescontosDTO.builder().vDescCond(0.00).vDescIncond(0.00).build())
//                                .trib(TributosDTO.builder()
//                                        .tribMun(TribMunicipalDTO.builder()
//                                                .tribISSQN(1)
//                                                .tpRetISSQN(1)
//                                                .vISSQN(5.00)
//                                                .vBC(250.00)
//                                                .pAliq(2.00)
//                                                .vLiq(250.00)
//                                                .build())
//                                        .tribFed(TribFederalDTO.builder()
//                                                .piscofins(PiscofinsDTO.builder()
//                                                        .CST("01")
//                                                        .vBCPisCofins(0.0)
//                                                        .pAliqPis(0.0)
//                                                        .pAliqCofins(0.0)
//                                                        .vPis(0.0)
//                                                        .vCofins(0.0)
//                                                        .tpRetPisCofins(1)
//                                                        .build())
//                                                .vRetCP(0.0)
//                                                .vRetCSLL(0.0)
//                                                .vRetIRRF(0.0)
//                                                .build())
//                                        .totTrib(TotalTributosDTO.builder()
//                                                .vTotTrib(ValoresTotaisTributoDTO.builder()
//                                                        .vTotTribFed(0.0)
//                                                        .vTotTribEst(0.0)
//                                                        .vTotTribMun(0.0)
//                                                        .build())
//                                                .pTotTrib(PercentuaisTotaisTributoDTO.builder()
//                                                        .pTotTribFed(0.0)
//                                                        .pTotTribEst(0.0)
//                                                        .pTotTribMun(0.0)
//                                                        .build())
//                                                .indTotTrib(0)
//                                                .pTotTribSN(0.0)
//                                                .build())
//                                        .build())
//                                .build())
//
//                        .build())
//                .build();
//    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmitirNfsePayload {
        private String ambiente;
        private Rps rps;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Rps {
        private String referencia;
        @JsonProperty("data_emissao")
        private String dataEmissao;
        private String competencia;
        @JsonProperty("natureza_tributacao")
        private int naturezaTributacao;
        private CpfCnpjDTO prestador;
        private Tomador tomador;
        private List<Servico> servicos;
        @JsonProperty("outras_informacoes")
        private String outrasInformacoes;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CpfCnpjDTO {
        @JsonProperty("cpf_cnpj")
        private String cpfCnpj;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Tomador {
        @JsonProperty("cpf_cnpj")
        private String cpfCnpj;
        @JsonProperty("inscricao_municipal")
        private String inscricaoMunicipal;
        @JsonProperty("nome_razao_social")
        private String nomeRazaoSocial;
        private String fone;
        private String email;
        private Endereco endereco;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Endereco {
        private String logradouro;
        private String numero;
        private String complemento;
        private String bairro;
        @JsonProperty("codigo_municipio")
        private String codigoMunicipio;
        private String cidade;
        private String uf;
        @JsonProperty("codigo_pais")
        private String codigoPais;
        private String pais;
        private String cep;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Servico {
        @JsonProperty("iss_retido")
        private boolean issRetido;
        @JsonProperty("responsavel_retencao")
        private int responsavelRetencao;
        @JsonProperty("item_lista_servico")
        private String itemListaServico;
        @JsonProperty("codigo_cnae")
        private String codigoCnae;
        @JsonProperty("codigo_tributacao_municipio")
        private String codigoTributacaoMunicipio;
        private String discriminacao;
        @JsonProperty("codigo_municipio")
        private String codigoMunicipio;
        @JsonProperty("codigo_pais")
        private String codigoPais;
        @JsonProperty("tipo_tributacao")
        private int tipoTributacao;
        @JsonProperty("exigibilidade_iss")
        private int exigibilidadeIss;
        @JsonProperty("codigo_municipio_incidencia")
        private String codigoMunicipioIncidencia;
        @JsonProperty("numero_processo")
        private String numeroProcesso;
        private String unidade;
        private int quantidade;
        private Valores valores;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Valores {
        @JsonProperty("valor_unitario")
        private double valorUnitario;
        @JsonProperty("valor_servicos")
        private double valorServicos;
        @JsonProperty("valor_iss")
        private double valorIss;
        @JsonProperty("valor_iss_retido")
        private double valorIssRetido;
        @JsonProperty("valor_liquido")
        private double valorLiquido;
        @JsonProperty("aliquota_iss")
        private double aliquotaIss;
        @JsonProperty("aliquota_pis")
        private double aliquotaPis;
        @JsonProperty("aliquota_cofins")
        private double aliquotaCofins;
        @JsonProperty("aliquota_inss")
        private double aliquotaInss;
        @JsonProperty("aliquota_ir")
        private double aliquotaIr;
        @JsonProperty("aliquota_csll")
        private double aliquotaCsll;
        @JsonProperty("desconto_incondicionado")
        private double descontoIncondicionado;
        @JsonProperty("desconto_condicionado")
        private double descontoCondicionado;
    }
    public String emitirNfseCompletaDps(EmitirNfseDpsRequest request) {
        // <<< impressão garantida >>>>
        System.out.println(">>> Entrou em emitirNfseCompletaDps <<<");

        // serialização do payload
        try {
            ObjectMapper mapper = new ObjectMapper()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String json = mapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(request);

            System.out.println("JSON a ser enviado →\n" + json);
        } catch (JsonProcessingException e) {
            System.out.println("Erro ao serializar payload: " + e.getMessage());
        }

        // resto do código
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authService.getAccessToken("nfse"));

        HttpEntity<EmitirNfseDpsRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.nuvemfiscal.com.br/nfse/dps",
                entity,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(
                    "Erro ao emitir NFS-e: "
                            + response.getStatusCode()
                            + " - "
                            + response.getBody()
            );
        }

        return response.getBody();
    }

}
