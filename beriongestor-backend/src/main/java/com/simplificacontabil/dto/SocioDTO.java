package com.simplificacontabil.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocioDTO {
    private Long id;
    private String nome;
    private String qualificacao;
    private String cpf;
}
