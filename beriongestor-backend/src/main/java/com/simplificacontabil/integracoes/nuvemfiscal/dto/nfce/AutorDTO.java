package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AutorDTO {
    @JsonProperty("cpf_cnpj")
    private String cpfCnpj;
}