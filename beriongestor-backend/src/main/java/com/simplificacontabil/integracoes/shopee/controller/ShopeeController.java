package com.simplificacontabil.integracoes.shopee.controller;

import com.simplificacontabil.integracoes.shopee.model.ShopeeToken;
import com.simplificacontabil.integracoes.shopee.repository.ShopeeTokenRepository;
import com.simplificacontabil.integracoes.shopee.util.ShopeeSigner;
import com.simplificacontabil.model.Usuario;
import com.simplificacontabil.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/shopee")
@RequiredArgsConstructor
public class ShopeeController {

    @Value("${partner-id}")
    private String partnerId;

    @Value("${partner-key}")
    private String partnerKey;

    @Value("${redirect-uri-shopee}")
    private String redirectUri;

    private final ShopeeTokenRepository repository;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/conectar")
    public ResponseEntity<Void> conectar() {
        long timestamp = System.currentTimeMillis() / 1000;
        String baseString = partnerId + redirectUri + timestamp;
        String sign = ShopeeSigner.generateSign(baseString, partnerKey);

        String url = "https://partner.shopeemobile.com/api/v2/shop/auth_partner" +
                "?partner_id=" + partnerId +
                "&redirect=" + redirectUri +
                "&timestamp=" + timestamp +
                "&sign=" + sign;

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
    }

    @PostMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam String code, @RequestParam("shop_id") String shopId) {
        long timestamp = System.currentTimeMillis() / 1000;
        String path = "/api/v2/auth/token/get";
        String baseString = partnerId + path + timestamp + shopId;
        String sign = ShopeeSigner.generateSign(baseString, partnerKey);

        Map<String, Object> body = Map.of(
                "code", code,
                "shop_id", Long.valueOf(shopId),
                "partner_id", Integer.valueOf(partnerId),
                "timestamp", timestamp,
                "sign", sign
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        RestTemplate rest = new RestTemplate();
        String url = "https://partner.shopeemobile.com/api/v2/auth/token/get";
        ResponseEntity<Map> response = rest.postForEntity(url, request, Map.class);

        Map respBody = response.getBody();
        String accessToken = (String) respBody.get("access_token");
        String refreshToken = (String) respBody.get("refresh_token");
        int expireIn = (Integer) respBody.get("expire_in");

        // Salva no banco
        ShopeeToken token = ShopeeToken.builder()
                .shopId(shopId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresAt(LocalDateTime.now().plusSeconds(expireIn))
                .usuario(getUsuarioLogado())
                .build();
        repository.save(token);

        return ResponseEntity.ok("Shopee conectada com sucesso.");
    }

    private Usuario getUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}