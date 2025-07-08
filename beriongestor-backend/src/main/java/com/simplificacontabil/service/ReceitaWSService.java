package com.simplificacontabil.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplificacontabil.dto.*;
import com.simplificacontabil.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReceitaWSService {
    private final String URL = "https://www.receitaws.com.br/v1/cnpj/";

    // Método que busca os dados do CNPJ e os converte para a entidade PessoaJuridica
    public PessoaJuridicaDTO buscarDadosCnpjDTO(String cnpj) {
        String url = URL + cnpj;
        RestTemplate restTemplate = new RestTemplate();

        JsonNode response = restTemplate.getForObject(url, JsonNode.class);
        assert response != null;
        return mapToPessoaJuridicaDTO(response);
    }


    private PessoaJuridicaDTO mapToPessoaJuridicaDTO(JsonNode node) {
        PessoaJuridicaDTO dto = new PessoaJuridicaDTO();

        dto.setCnpj(node.path("cnpj").asText());
        dto.setRazaoSocial(node.path("nome").asText());
        dto.setNomeFantasia(node.path("fantasia").asText());
        dto.setSituacao(node.path("situacao").asText());
        dto.setTipo(node.path("tipo").asText());
        dto.setNaturezaJuridica(node.path("natureza_juridica").asText());
        dto.setPorte(node.path("porte").asText());
        dto.setDataAbertura(node.path("abertura").asText());
        dto.setUltimaAtualizacao(node.path("ultima_atualizacao").asText());

        // Atividades principais
        List<AtividadeDTO> atividadesPrincipais = new ArrayList<>();
        if (node.has("atividade_principal")) {
            for (JsonNode atividadeNode : node.get("atividade_principal")) {
                AtividadeDTO atividade = new AtividadeDTO();
                atividade.setCodigo(atividadeNode.path("code").asText());
                atividade.setDescricao(atividadeNode.path("text").asText());
                atividadesPrincipais.add(atividade);
            }
        }
        dto.setAtividadesPrincipais(atividadesPrincipais);

        // Atividades secundárias
        List<AtividadeDTO> atividadesSecundarias = new ArrayList<>();
        if (node.has("atividades_secundarias")) {
            for (JsonNode atividadeNode : node.get("atividades_secundarias")) {
                AtividadeDTO atividade = new AtividadeDTO();
                atividade.setCodigo(atividadeNode.path("code").asText());
                atividade.setDescricao(atividadeNode.path("text").asText());
                atividadesSecundarias.add(atividade);
            }
        }
        dto.setAtividadesSecundarias(atividadesSecundarias);

        // Sócios
        List<SocioDTO> socios = new ArrayList<>();
        if (node.has("qsa")) {
            for (JsonNode socioNode : node.get("qsa")) {
                SocioDTO socio = new SocioDTO();
                socio.setNome(socioNode.path("nome").asText());
                socio.setQualificacao(socioNode.path("qual").asText());
                socios.add(socio);
            }
        }
        dto.setSocios(socios);

        // Endereço
        EnderecoDTO endereco = new EnderecoDTO();
        endereco.setLogradouro(node.path("logradouro").asText());
        endereco.setNumero(node.path("numero").asText());
        endereco.setComplemento(node.path("complemento").asText());
        endereco.setBairro(node.path("bairro").asText());
        endereco.setMunicipio(node.path("municipio").asText());
        endereco.setUf(node.path("uf").asText());
        endereco.setCep(node.path("cep").asText());
        dto.setEndereco(endereco);

        // Simples Nacional
        // Simples Nacional
        if (node.has("simples")) {
            JsonNode simplesNode = node.get("simples");
            SimplesNacDTO simples = new SimplesNacDTO();
            simples.setOptante(simplesNode.path("simples").asBoolean(false));

            dto.setSimples(simples);
        }


        return dto;
    }
    private List<Atividade> mapAtividades(JsonNode node) {
        List<Atividade> atividades = new ArrayList<>();
        if (node.has("atividade_principal")) {
            JsonNode atividadesNode = node.get("atividade_principal");
            for (JsonNode atividadeNode : atividadesNode) {
                Atividade atividade = new Atividade();
                atividade.setCodigo(atividadeNode.get("code").asText());
                atividade.setDescricao(atividadeNode.get("text").asText());
                atividades.add(atividade);
            }
        }
        return atividades;
    }

    private List<Atividade> mapAtividadesPrincipais(JsonNode node) {
        List<Atividade> atividadesPrincipais = new ArrayList<>();
        if (node.has("atividade_principal")) {
            JsonNode atividadesNode = node.get("atividade_principal");
            for (JsonNode atividadeNode : atividadesNode) {
                Atividade atividade = new Atividade();
                atividade.setCodigo(atividadeNode.get("code").asText());
                atividade.setDescricao(atividadeNode.get("text").asText());
                atividadesPrincipais.add(atividade);
            }
        }
        return atividadesPrincipais;
    }

    private List<Atividade> mapAtividadesSecundarias(JsonNode node) {
        List<Atividade> atividadesSecundarias = new ArrayList<>();
        if (node.has("atividades_secundarias")) {
            JsonNode atividadesNode = node.get("atividades_secundarias");
            for (JsonNode atividadeNode : atividadesNode) {
                Atividade atividade = new Atividade();
                atividade.setCodigo(atividadeNode.get("code").asText());
                atividade.setDescricao(atividadeNode.get("text").asText());
                atividadesSecundarias.add(atividade);
            }
        }
        return atividadesSecundarias;
    }

    private List<Socio> mapSocios(JsonNode node) {
        List<Socio> socios = new ArrayList<>();
        if (node.has("qsa")) {
            JsonNode sociosNode = node.get("qsa");
            for (JsonNode socioNode : sociosNode) {
                Socio socio = new Socio();
                socio.setNome(socioNode.get("nome").asText());
                socio.setCpf(socioNode.get("qual").asText()); // Mapear CPF
                socios.add(socio);
            }
        }
        return socios;
    }

    private SimplesNac mapSimples(JsonNode node) {
        SimplesNac simples = new SimplesNac();
        if (node.has("simples")) {
            JsonNode simplesNode = node.get("simples");
            simples.setOptante(simplesNode.get("optante").asBoolean());
            simples.setDataExclusao(simplesNode.get("data_exclusao").asText());
        }
        return simples;
    }

    private Endereco mapEndereco(JsonNode node) {
        Endereco endereco = new Endereco();
        if (node.has("logradouro")) {
            endereco.setLogradouro(node.get("logradouro").asText());
            endereco.setNumero(node.get("numero").asText());
            endereco.setComplemento(node.get("complemento").asText());
            endereco.setBairro(node.get("bairro").asText());
            endereco.setCodigoIbge(node.get("municipio").asText());
            endereco.setUf(node.get("uf").asText());
            endereco.setCep(node.get("cep").asText());
        }
        return endereco;
    }

    // Método para mapear o JsonNode para a entidade PessoaJuridica
    private PessoaJuridica mapToPessoaJuridica(JsonNode node) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Mapeando as informações principais de PessoaJuridica
            PessoaJuridica pessoaJuridica = objectMapper.treeToValue(node, PessoaJuridica.class);

            // Mapear atividades
            pessoaJuridica.setAtividadesPrincipais(mapAtividadesPrincipais(node));
            pessoaJuridica.setAtividadesSecundarias(mapAtividadesSecundarias(node));

            // Mapear sócios
            pessoaJuridica.setSocios(mapSocios(node));

            // Mapear simples
            pessoaJuridica.setSimples(mapSimples(node));

            // Mapear endereço
            pessoaJuridica.setEndereco(mapEndereco(node));


            // Corrigir relacionamentos bidirecionais
            for (Atividade atividade : pessoaJuridica.getAtividadesPrincipais()) {
                atividade.setPessoaJuridica(pessoaJuridica);
            }
            for (Atividade atividade : pessoaJuridica.getAtividadesSecundarias()) {
                atividade.setPessoaJuridica(pessoaJuridica);
            }

            return pessoaJuridica;
        } catch (Exception e) {
            System.err.println("Erro ao mapear dados da Pessoa Jurídica: " + e.getMessage());
            throw new RuntimeException("Falha ao processar dados da Pessoa Jurídica", e);
        }
    }

}
