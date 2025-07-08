package com.simplificacontabil.service;

import com.simplificacontabil.config.CloudflareConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudflareService {

    private final CloudflareConfig config;
    private final RestTemplate restTemplate = new RestTemplate();

    public void banIp(String ip, String motivo) {
        String url = "https://api.cloudflare.com/client/v4/user/firewall/access_rules/rules";

        var headers = new HttpHeaders();
        headers.setBearerAuth(config.getApiToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> configMap = Map.of(
                "target", "ip",
                "value", ip
        );

        Map<String, Object> payload = Map.of(
                "mode", "block",
                "configuration", configMap,
                "notes", motivo
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("✅ IP banido com sucesso: {}", ip);
            } else {
                log.warn("⚠️ Falha ao banir IP: {} | Status: {}", ip, response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("❌ Erro ao tentar banir IP {}: {}", ip, e.getMessage());
        }
    }
}
