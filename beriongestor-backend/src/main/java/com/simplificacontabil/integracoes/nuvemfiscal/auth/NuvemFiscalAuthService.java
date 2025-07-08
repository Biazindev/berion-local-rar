package com.simplificacontabil.integracoes.nuvemfiscal.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class NuvemFiscalAuthService {

    @Value("${nuvemfiscal.client-id}")
    private String clientId;

    @Value("${nuvemfiscal.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    private final Map<String, String> cachedTokens = new ConcurrentHashMap<>();
    private final Map<String, Instant> expiracoes = new ConcurrentHashMap<>();

    private static final String TOKEN_URL = "https://auth.nuvemfiscal.com.br/oauth/token";

    public String getAccessToken(String scope) {
        Instant expira = expiracoes.get(scope);
        if (!cachedTokens.containsKey(scope) || expira == null || Instant.now().isAfter(expira.minusSeconds(60))) {
            log.info("🔑 Buscando novo token com scope [{}]", scope);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);
            body.add("grant_type", "client_credentials");
            body.add("scope", scope);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            try {
                log.info("➡️  Requisição de token enviada para [{}]", TOKEN_URL);
                log.info("📤 Headers: {}", new ObjectMapper().writeValueAsString(headers.toSingleValueMap()));
                log.info("📤 Body: {}", new ObjectMapper().writeValueAsString(body));

                ResponseEntity<Map> response = restTemplate.exchange(
                        TOKEN_URL,
                        HttpMethod.POST,
                        request,
                        Map.class
                );

                log.info("⬅️  Status: {}", response.getStatusCode());
                log.info("⬅️  Response Headers: {}", response.getHeaders());

                if (response.hasBody()) {
                    log.info("⬅️  Response Body: {}", new ObjectMapper().writeValueAsString(response.getBody()));
                } else {
                    log.warn("⚠️  Resposta sem corpo.");
                }

                if (response.getStatusCode().is2xxSuccessful()) {
                    Map<String, Object> bodyMap = response.getBody();
                    String token = (String) bodyMap.get("access_token");
                    int expiresIn = (int) bodyMap.get("expires_in");

                    cachedTokens.put(scope, token);
                    expiracoes.put(scope, Instant.now().plusSeconds(expiresIn));

                    log.info("✅ Token [{}] obtido com sucesso. Expira em: {}", scope, expiracoes.get(scope));
                } else {
                    log.error("❌ Falha ao obter token [{}]: HTTP {}", scope, response.getStatusCode());
                }
            } catch (Exception e) {
                log.error("❌ Erro ao buscar token da Nuvem Fiscal [{}]: {}", scope, e.getMessage(), e);
            }
        } else {
            log.info("🕓 Token de [{}] ainda válido. Expira em {}", scope, expiracoes.get(scope));
        }

        return cachedTokens.get(scope);
    }
}
