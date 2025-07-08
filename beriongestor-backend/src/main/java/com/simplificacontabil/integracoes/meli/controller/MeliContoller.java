package com.simplificacontabil.integracoes.meli.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.simplificacontabil.integracoes.meli.dto.PedidoMLDTO;
import com.simplificacontabil.integracoes.meli.model.MercadoLivreToken;
import com.simplificacontabil.integracoes.meli.repository.MercadoLivreTokenRepository;
import com.simplificacontabil.integracoes.meli.service.MeliService;
import com.simplificacontabil.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;



@RequestMapping("/api/meeli")
@RestController
public class MeliContoller {


    @Autowired
    private  MeliService service;

    @Autowired
    private  MercadoLivreTokenRepository repository;

    @Autowired
    private  RestTemplate restTemplate;

    @Autowired
    private  UsuarioService usuarioService;


    @Value("${client-id}")
    private String CLIENT_ID;

    @Value("${client-secret}")
    private String CLIENT_SECRET;

    @Value("${redirect-uri}")
    private String REDIRECT_URI;


    @GetMapping("/conectar")
    public ResponseEntity<Void> conectar() {
        String redirectUrl = "https://auth.mercadolivre.com.br/authorization" +
                "?response_type=code" +
                "&client_id=" + CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URI;
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUrl)).build();
    }
    @PostMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam String code) {
        String url = "https://api.mercadolibre.com/oauth/token";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", CLIENT_ID);
        form.add("client_secret", CLIENT_SECRET);
        form.add("code", code);
        form.add("redirect_uri", REDIRECT_URI);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        String accessToken = (String) response.getBody().get("access_token");
        String refreshToken = (String) response.getBody().get("refresh_token");
        Integer expiresIn = (Integer) response.getBody().get("expires_in");
        String userId = String.valueOf(response.getBody().get("user_id"));

        // Salvar no banco
        MercadoLivreToken token = MercadoLivreToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .mlUserId(userId)
                .expiresAt(LocalDateTime.now().plusSeconds(expiresIn))
                .usuarioDoERP(usuarioService.getUsuarioAutenticado()) // Obtém o usuário autenticado no ERP
                .build();
        repository.save(token);

        return ResponseEntity.ok("Conta conectada com sucesso!");
    }
    public String renovarTokenSeExpirado(MercadoLivreToken token) {
        if (token.getExpiresAt().isAfter(LocalDateTime.now().minusMinutes(2))) {
            return token.getAccessToken();
        }

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("client_id", CLIENT_ID);
        form.add("client_secret", CLIENT_SECRET);
        form.add("refresh_token", token.getRefreshToken());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity("https://api.mercadolibre.com/oauth/token", request, Map.class);

        token.setAccessToken((String) response.getBody().get("access_token"));
        token.setRefreshToken((String) response.getBody().get("refresh_token"));
        token.setExpiresAt(LocalDateTime.now().plusSeconds((Integer) response.getBody().get("expires_in")));

        repository.save(token);
        return token.getAccessToken();
    }
    public List<PedidoMLDTO> buscarPedidos(MercadoLivreToken token) {
        String accessToken = renovarTokenSeExpirado(token);
        String url = "https://api.mercadolibre.com/orders/search?seller=" + token.getMlUserId();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, request, JsonNode.class);
        assert response.getBody() != null;
        return service.parsePedidos(response.getBody());
    }


}
