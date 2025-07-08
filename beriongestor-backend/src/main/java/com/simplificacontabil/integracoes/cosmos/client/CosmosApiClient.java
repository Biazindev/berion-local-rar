package com.simplificacontabil.integracoes.cosmos.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CosmosApiClient {

    private static final String BASE_URL = "https://api.cosmos.bluesoft.io";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String token;

    public CosmosApiClient(@Value("${cosmos.token}") String token) {
        this.token = token;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public JsonNode getProductByGtin(String gtin) throws IOException, InterruptedException {
        String url = String.format("%s/gtins/%s.json", BASE_URL, gtin);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("User-Agent", "MeuERP/1.0")
                .header("X-Cosmos-Token", token)
                .GET()
                .build();

        HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) {
            throw new RuntimeException(
                    "Erro ao buscar GTIN " + gtin + ": HTTP " + resp.statusCode()
            );
        }
        return objectMapper.readTree(resp.body());
    }
}
