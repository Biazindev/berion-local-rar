package com.simplificacontabil.integracoes.nuvemfiscal.client.nfse;

import com.simplificacontabil.integracoes.nuvemfiscal.auth.NuvemFiscalAuthService;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse.EmitirNfseDpsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;


@Component
@RequiredArgsConstructor
public class NuvemFiscalNfseClient {

    private final RestTemplate restTemplate;
    private final NuvemFiscalAuthService authService;

    private static final String BASE_URL_NFSE = "https://api.nuvemfiscal.com.br/nfse/dps";
    private static final String BASE_URL_CONSULTA_NFSE = "https://api.nuvemfiscal.com.br/nfse/";



    /**
     * Emite um DPS para NFS-e (endpoint /v1/nfse/dps).
     */
    public String emitirNfseDps(EmitirNfseDpsRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authService.getAccessToken("nfse"));

        HttpEntity<EmitirNfseDpsRequest> entity = new HttpEntity<>(request, headers);
        String url = BASE_URL_NFSE + "/dps";

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(
                    "Erro ao emitir DPS NFS-e: " +
                            response.getStatusCode() + " - " +
                            response.getBody()
            );
        }
        return response.getBody();
    }


    /**
     * Consulta o status / retorno de um DPS já enviado.
     * Retorna todo o JSON como String; você pode desserializar num DTO próprio.
     */
    public String consultarNfse(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authService.getAccessToken("nfse"));

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String url = BASE_URL_CONSULTA_NFSE + "/" + id;

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(
                    "Erro ao consultar NFS-e DPS: " +
                            response.getStatusCode() + " - " +
                            response.getBody()
            );
        }
        return response.getBody();
    }

    public String baixarXmlDps(String idNfse) {
        String url = BASE_URL_NFSE + "/" + idNfse + "/xml/dps";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authService.getAccessToken("nfse")); // ou jwt direto
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody(); // Isso aqui será o XML como string
    }


    // método genérico usado pelos dois primeiros (NFe e NFSe sem DPS)
    private String emitirComEscopo(Object request, String scope, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authService.getAccessToken(scope));

        HttpEntity<Object> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(
                    "Erro ao emitir NF com escopo [" + scope + "]: " +
                            response.getStatusCode() + " - " +
                            response.getBody()
            );
        }
        return response.getBody();
    }


    /**
     * Baixa o PDF da DANFSE e retorna como array de bytes.
     */
    public byte[] baixarDanfePdf(String idNfse) {
        String url = "https://api.nuvemfiscal.com.br/nfse/" + idNfse + "/pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authService.getAccessToken("nfse"));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_PDF));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                byte[].class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException(
                    "Erro ao baixar PDF da DANFSE: " +
                            response.getStatusCode() + " - " +
                            response.getBody()
            );
        }

        return response.getBody();
    }




}

