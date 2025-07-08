package com.simplificacontabil.integracoes.nuvemfiscal.client.empresa;

import com.simplificacontabil.integracoes.nuvemfiscal.auth.NuvemFiscalAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class NuvemFIscalEmpresaClient {

    @Autowired
    private  NuvemFiscalAuthService authService;
    @Autowired
    private final RestTemplate restTemplate;

    public NuvemFIscalEmpresaClient(NuvemFiscalAuthService authService, RestTemplate restTemplate) {
        this.authService = authService;
        this.restTemplate = restTemplate;
    }

    private static final String BASE_URL_EMPRESAS = "https://api.nuvemfiscal.com.br/empresas";


    public String consultarConfigNfse(String cpfCnpj) {
        String url = BASE_URL_EMPRESAS + "/" + cpfCnpj + "/nfse";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // o token deve ter scope "nfse"
        headers.setBearerAuth(authService.getAccessToken("empresa"));

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> resp = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
        return resp.getBody();
    }
    public String atualizarConfigNfse(
            String cpfCnpj,
            String novoToken,
            int lote,
            String serie,
            int numero,
            String ambiente
    ) {
        String url = BASE_URL_EMPRESAS + "/" + cpfCnpj + "/nfse";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authService.getAccessToken("empresa"));

        // Monta o bloco obrigatório de RPS
        Map<String,Object> rpsBlock = Map.of(
                "lote",   lote,
                "serie",  serie,
                "numero", numero
        );

        // Monta o body com os campos obrigatórios + token + ambiente
        Map<String,Object> body = Map.of(
                "rps",        rpsBlock,
                "prefeitura", Map.of("token", novoToken),
                "ambiente",   ambiente
        );

        HttpEntity<Map<String,Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> resp = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                String.class
        );

        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(
                    "Erro ao atualizar config NFS-e: " +
                            resp.getStatusCode() + " - " + resp.getBody()
            );
        }
        return resp.getBody();
    }
    public String atualizarRegimeTributarioNfse(
            String cpfCnpj,
            int opSimpNac,
            int regApTribSN,
            int regEspTrib,
            int lote,
            String serie,
            int numero,
            String ambiente
    ) {
        String url = BASE_URL_EMPRESAS + "/" + cpfCnpj + "/nfse";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authService.getAccessToken("empresa"));

        // Bloco obrigatório do RPS
        Map<String, Object> rpsBlock = Map.of(
                "lote", lote,
                "serie", serie,
                "numero", numero
        );

        // Bloco do regime tributário
        Map<String, Object> regimeTributarioBlock = Map.of(
                "opSimpNac", opSimpNac,
                "regApTribSN", regApTribSN,
                "regEspTrib", regEspTrib
        );

        // Monta o body final
        Map<String, Object> body = Map.of(
                "rps", rpsBlock,
                "regTrib", regimeTributarioBlock,
                "ambiente", ambiente
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> resp = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                String.class
        );

        return resp.getBody();
    }
}
