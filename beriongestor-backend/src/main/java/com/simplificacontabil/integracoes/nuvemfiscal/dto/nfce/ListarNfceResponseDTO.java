package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ListarNfceResponseDTO {
    @JsonProperty("@count")
    private Integer total;
    private List<NfceDTO> data;
}
