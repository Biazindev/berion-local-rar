package com.simplificacontabil.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Pessoa {
    private String nome;
    private String email;
    private String telefone;
    private Endereco endereco;
}
