package com.simplificacontabil.integracoes.nuvemfiscal.builder.nfse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplificacontabil.config.NfseConfigEmpresa;
import com.simplificacontabil.dto.EmitirNfseRequestDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.auth.NuvemFiscalAuthService;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse.*;
import com.simplificacontabil.repository.NfseConfigEmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class NfsePayloadBuilder {

    private final NuvemFiscalAuthService authService;
    private final RestTemplate restTemplate = new RestTemplate();



    private final NfseConfigEmpresaRepository configRepository;

    public EmitirNfseDpsRequest montarPayload(Long empresaId, EmitirNfseRequestDTO dto) {
        NfseConfigEmpresa config = configRepository.findById(empresaId)
                .orElseThrow(() -> new RuntimeException("Empresa não configurada para emissão de NFS-e"));

        return EmitirNfseDpsRequest.builder()
                .provedor(config.getProvedor())
                .ambiente(config.getAmbiente())
                .referencia("DPS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .infDPS(InfDpsDTO.builder()
                        .tpAmb(config.getTipoAmbiente())
                        .dhEmi(Instant.now().toString())
                        .verAplic("1.0")
                        .dCompet(LocalDate.now().toString())
                        .prest(PrestadorDTO.builder()
                                .CNPJ(config.getCnpj())
//                                .IM(config.getInscricaoMunicipal())
                                .regTrib(new RegimeTributario()) // deixar vazio!
                                .build())
                        .toma(TomadorDTO.builder()
                                .CPF(dto.getCpfCnpjTomador())
                                .xNome(dto.getNomeTomador())
                                .fone(dto.getTelefone())
                                .email(dto.getEmail())
                                .end(EnderecoCompleto.builder()
                                        .xLgr(dto.getEndereco().getLogradouro())
                                        .tpLgr("Rua")
                                        .nro(dto.getEndereco().getNumero())
                                        .xBairro(dto.getEndereco().getBairro())
                                        .endNac(EnderecoNacional.builder()
                                                .cMun(dto.getEndereco().getCodigoIbge())
                                                .CEP(dto.getEndereco().getCep())
                                                .build())
                                        .build())
                                .build())
                        .serv(ServicoDTO.builder()
                                .locPrest(LocalPrestacao.builder()
                                        .cLocPrestacao(dto.getEndereco().getCodigoIbge())
                                        .cPaisPrestacao("BR")
                                        .build())
                                .cServ(CodigoServico.builder()
                                        .cTribMun(dto.getServico().getCodigoTributacaoMunicipal())
                                        .cTribNac(dto.getServico().getCodigoTributacaoNacional())
                                        .CNAE(dto.getServico().getCnae())
                                        .xDescServ(dto.getServico().getDescricao())
                                        .cNBS(dto.getServico().getNbs())
                                        .build())
                                .infoCompl(InfoComplDTO.builder()
                                        .xInfComp(dto.getServico().getInformacoesComplementares())
                                        .build())
                                .build())
                        .valores(ValoresDTO.builder()
                                .vServPrest(ValorServPrestDTO.builder()
                                        .vServ(dto.getServico().getValor())
                                        .vReceb(dto.getServico().getValor())
                                        .build())
                                .vDescCondIncond(ValorDescontosDTO.builder()
                                        .vDescCond(0.0)
                                        .vDescIncond(0.0)
                                        .build())
                                .trib(TributosDTO.builder()
                                        .tribMun(TribMunicipalDTO.builder()
                                                .tribISSQN(1)
                                                .tpRetISSQN(1)
                                                .cLocIncid(dto.getEndereco().getCodigoIbge())
                                                .cPaisResult("BR")
                                                .vBC(null)
                                                .pAliq(null)
                                                .vISSQN(null)
                                                .vLiq(null)
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();
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
