package com.simplificacontabil.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;
    private String inscricaoEstadual;
    private String inscricaoMunicipal;
    private String regimeTributario;
    private String cnae;
    private Integer crt; //Código de regime tributário EX: - 1	Simples Nacional, 2	Simples Nacional - excesso sublimite, 3	Regime Normal
    private String fone;
    private Integer serieNfe;
    private String ambiente;



    @Embedded
    private Endereco endereco;
}
