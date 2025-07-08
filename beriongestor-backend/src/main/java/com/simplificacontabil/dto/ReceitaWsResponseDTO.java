package com.simplificacontabil.dto;

import com.simplificacontabil.model.Atividade;
import com.simplificacontabil.model.SimplesNac;
import com.simplificacontabil.model.Socio;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceitaWsResponseDTO {
    private String abertura;  // Data de abertura
    private String situacao;  // Situação da empresa (ex: ATIVA)
    private String tipo;      // Tipo (ex: MATRIZ)
    private String nome;      // Nome da empresa
    private String porte;     // Porte da empresa (ex: MICRO EMPRESA)
    private String naturezaJuridica;  // Natureza jurídica (ex: Sociedade Empresária Limitada)

    // Atividades principais e secundárias
    private Atividade atividadePrincipal;  // Atividade principal (1º item do array)
    private Atividade[] atividadesSecundarias;  // Atividades secundárias (array de objetos)

    // Informações do sócio
    private Socio[] qsa;  // Lista de sócios

    // Endereço da empresa
    private String logradouro;
    private String numero;
    private String complemento;
    private String municipio;
    private String bairro;
    private String uf;
    private String cep;

    // Dados de contato
    private String email;
    private String telefone;

    // Capital social e dados relacionados
    private String capitalSocial;

    // Dados do Simples Nacional
    private SimplesNac simples;

    // Outros campos
    private String cnpj;
    private String fantasia;  // Nome fantasia (caso exista)
    private String efr;
    private String motivoSituacao;
    private String situacaoEspecial;
    private String dataSituacaoEspecial;
    private String ultimaAtualizacao;
    private String status;  // Status da consulta (OK)
}
