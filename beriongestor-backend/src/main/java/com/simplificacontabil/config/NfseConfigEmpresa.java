package com.simplificacontabil.config;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NfseConfigEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String razaoSocial;
    private String cnpj;
    private String inscricaoMunicipal;
    private String regimeTributario; // ex: "Simples Nacional"

    private String provedor; // ex: "proEloTech"
    private String ambiente; // "homologacao" ou "producao"
    private Integer tipoAmbiente;
    private String caminhoCertificado; // caminho absoluto .pfx
    private String senhaCertificado; // você pode criptografar isso com AES
}
