package com.simplificacontabil.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class Configuracao {
    @Id
    private String chave;
    private String valor;
}
