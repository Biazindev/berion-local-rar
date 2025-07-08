package com.simplificacontabil.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplificacontabil.dto.PagamentoTefDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class PaygoTefService {

    private final ObjectMapper objectMapper;

    public String iniciarPagamento(PagamentoTefDTO dto) {
        try {
            String json = objectMapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/paygo")) // mesma porta do teu back atual
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (Exception e) {
            throw new RuntimeException("Falha na comunicação com o middleware TEF: " + e.getMessage(), e);
        }
    }
}
