package com.simplificacontabil.dto;

import lombok.Data;

import java.util.List;

@Data

public class ReceitaDTO {
    private Long id;
    private String nome;
    private Long produtoFinalId;
    private String produtoFinalNome;
    private List<ItemReceitaDTO> itens;
}