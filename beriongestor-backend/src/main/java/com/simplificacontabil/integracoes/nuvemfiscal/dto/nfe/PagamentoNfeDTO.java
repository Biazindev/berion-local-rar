package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import lombok.*;
import com.fasterxml.jackson.annotation.*;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagamentoNfeDTO {
    private List<DetPagamentoDTO> detPag;
    private Double vTroco;
}