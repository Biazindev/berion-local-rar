package com.simplificacontabil.dto;

import com.simplificacontabil.model.ItemVenda;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class NfeDTO {

    private String cnpjEmitente;
    private String nomeEmitente;
    private String enderecoEmitente;
    private List<ItemVenda> itens; // ou outro nome que represente os produtos
    private double totalImpostos;

}
