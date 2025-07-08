package com.simplificacontabil.service;

import com.simplificacontabil.model.CepResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class CepService {

    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/";

    @Autowired
    private RestTemplate restTemplate;

    // Método para consultar o CEP
    public CepResponse consultarCep(String cep) {
        String url = VIA_CEP_URL + cep + "/json/";

        // Faz a requisição GET para a API ViaCEP
        ResponseEntity<CepResponse> response = restTemplate.getForEntity(url, CepResponse.class);

        return response.getBody();
    }
}
