package com.simplificacontabil.integracoes.nuvemfiscal.client.nfe;

import com.simplificacontabil.integracoes.nuvemfiscal.auth.NuvemFiscalAuthService;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe.EmitirNfeRequestDTO;
import com.simplificacontabil.model.Empresa;
import com.simplificacontabil.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Component
public class NuvemFiscalNfeClient {

    @Autowired
    private NuvemFiscalAuthService authService;
    @Autowired
    private EmpresaRepository empresaRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL_DISTRIBUICAO_NFE = "https://api.nuvemfiscal.com.br/distribuicao/nfe";
    private static final String BASE_URL_NFE = "https://api.nuvemfiscal.com.br/nfe";


    public String emitirNfe(EmitirNfeRequestDTO request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authService.getAccessToken("nfe"));

        HttpEntity<EmitirNfeRequestDTO> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL_NFE, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao emitir NF-e: " +
                    response.getStatusCode() + " - " + response.getBody());
        }

        return response.getBody();
    }
    public String consultarNfe(String id) {
        String url = BASE_URL_NFE + "/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authService.getAccessToken("nfe"));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao consultar NF-e: " +
                    response.getStatusCode() + " - " + response.getBody());
        }

        return response.getBody();
    }


    public byte[] baixarDanfeNfe(String idNfe) {
        String url = BASE_URL_NFE + "/" + idNfe + "/pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authService.getAccessToken("nfe"));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_PDF));

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, byte[].class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Erro ao baixar DANFE: " +
                    response.getStatusCode() + " - " + Arrays.toString(response.getBody()));
        }

        return response.getBody();
    }

    public String listarDocumentosDistribuicaoNfe(
            String cpfCnpj,
            String ambiente,
            Long distNsu,
            String tipoDocumento,
            String formaDistribuicao,
            String chaveAcesso,
            Integer top,
            Integer skip,
            Boolean inlinecount
    ) {
        Empresa empresa = empresaRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Empresa com ID 1 não encontrada"));

        if (empresa.getCnpj() == null) {
            throw new RuntimeException("Empresa não possui documento (CNPJ/CPF) cadastrado");
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(BASE_URL_NFE)
                .queryParam("cpf_cnpj", empresa.getCnpj())
                .queryParam("ambiente", empresa.getAmbiente());

        // Adiciona os parâmetros opcionais apenas se não forem nulos
        Optional.ofNullable(distNsu)
                .ifPresent(nsu -> uriBuilder.queryParam("dist_nsu", nsu));
        Optional.ofNullable(tipoDocumento)
                .ifPresent(td -> uriBuilder.queryParam("tipo_documento", td));
        Optional.ofNullable(formaDistribuicao)
                .ifPresent(fd -> uriBuilder.queryParam("forma_distribuicao", fd));
        Optional.ofNullable(chaveAcesso)
                .ifPresent(ca -> uriBuilder.queryParam("chave_acesso", ca));
        Optional.ofNullable(top)
                .ifPresent(t -> uriBuilder.queryParam("$top", t));
        Optional.ofNullable(skip)
                .ifPresent(s -> uriBuilder.queryParam("$skip", s));
        Optional.ofNullable(inlinecount)
                .ifPresent(ic -> uriBuilder.queryParam("$inlinecount", ic));

        String url = uriBuilder.build(true).toUriString(); // Codifica corretamente os parâmetros

        // Obtém o token atualizado
        String token = authService.getAccessToken("nfe"); // Alterado de "distribuicao" para "nfe"

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Erro ao listar documentos de distribuição de NF-e: " +
                        response.getStatusCode() + " - " + response.getBody());
            }

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao comunicar com a API da Nuvem Fiscal: " + e.getMessage(), e);
        }
    }



}
