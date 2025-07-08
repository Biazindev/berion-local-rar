package com.simplificacontabil.integracoes.nuvemfiscal.client.nfce;

import com.simplificacontabil.integracoes.nuvemfiscal.auth.NuvemFiscalAuthService;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce.CancelarNfceRequestDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce.CancelarNfceResponseDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce.EmitirNfceRequestDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce.EmitirNfceResponseDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce.ListarNfceResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Arrays;

@Component
public class NuvemFiscalNfceClient {

    @Autowired
    private NuvemFiscalAuthService authService;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String BASE_URL_NFCE = "https://api.nuvemfiscal.com.br/nfce";

    public ListarNfceResponseDTO listarNfce(
            String cpfCnpj,
            String ambiente,
            Integer top,
            Integer skip,
            Boolean inlinecount,
            String referencia,
            String chave,
            Integer serie
    ) {
        String token = authService.getAccessToken("nfce");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(BASE_URL_NFCE)
                .queryParam("cpf_cnpj", cpfCnpj)
                .queryParam("ambiente", ambiente);

        if (top != null) builder.queryParam("$top", top);
        if (skip != null) builder.queryParam("$skip", skip);
        if (inlinecount != null) builder.queryParam("$inlinecount", inlinecount);
        if (referencia != null) builder.queryParam("referencia", referencia);
        if (chave != null) builder.queryParam("chave", chave);
        if (serie != null) builder.queryParam("serie", serie);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<ListarNfceResponseDTO> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                ListarNfceResponseDTO.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao listar NFC-e: " + response.getStatusCode() + " - " + response.getBody());
        }
        return response.getBody();
    }

    public EmitirNfceResponseDTO emitirNfce(EmitirNfceRequestDTO request) {
        String token = authService.getAccessToken("nfce");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<EmitirNfceRequestDTO> entity = new HttpEntity<>(request, headers);
        ResponseEntity<EmitirNfceResponseDTO> response = restTemplate.postForEntity(
                BASE_URL_NFCE, entity, EmitirNfceResponseDTO.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Erro ao emitir NFC-e: " + response.getStatusCode() + " - " + response.getBody());
        }
        return response.getBody();
    }

    public CancelarNfceResponseDTO cancelarNfce(String id, CancelarNfceRequestDTO request) {
        String token = authService.getAccessToken("nfce");
        String url = BASE_URL_NFCE + "/" + id + "/cancelamento";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<CancelarNfceRequestDTO> entity = new HttpEntity<>(request, headers);
        ResponseEntity<CancelarNfceResponseDTO> response = restTemplate.postForEntity(
                url, entity, CancelarNfceResponseDTO.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Erro ao cancelar NFC-e: " + response.getStatusCode() + " - " + response.getBody());
        }
        return response.getBody();
    }

    public String consultarNfce(String id) {
        String token = authService.getAccessToken("nfce");
        String url = BASE_URL_NFCE + "/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao consultar NFC-e: " + response.getStatusCode() + " - " + response.getBody());
        }
        return response.getBody();
    }

    public byte[] baixarDanfeNfce(String id) {
        String token = authService.getAccessToken("nfce");
        String url = BASE_URL_NFCE + "/" + id + "/pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_PDF));

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, byte[].class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Erro ao baixar DANFE NFC-e: " + response.getStatusCode() + " - " + Arrays.toString(response.getBody()));
        }
        return response.getBody();
    }
}
