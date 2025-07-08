package com.simplificacontabil.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Emitente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String inscricaoEstadual;
    private String logradouro;
    private String numero;
    private String bairro;
    private String municipio;
    private String codigoMunicipio;
    private String uf;
    private String cep;
    private String regimeTributario;

}
