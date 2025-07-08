package com.simplificacontabil.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.simplificacontabil.model.Endereco;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {

    public Endereco mapEndereco(JsonNode node) {
        return Endereco.builder()
                .logradouro(node.get("logradouro").asText())
                .numero(node.get("numero").asText())
                .complemento(node.get("complemento").asText())
                .bairro(node.get("bairro").asText())
                .codigoIbge(node.get("municipio").asText())
                .uf(node.get("uf").asText())
                .cep(node.get("cep").asText())
                .build();
    }
}
