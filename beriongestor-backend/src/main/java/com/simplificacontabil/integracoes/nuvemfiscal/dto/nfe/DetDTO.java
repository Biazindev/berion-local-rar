package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import lombok.*;
import com.fasterxml.jackson.annotation.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetDTO {

    @JsonProperty("nItem")
    private Integer nItem;

    @JsonProperty("prod")
    private ProdutoNfeDTO prod;

    @JsonProperty("imposto")
    private ImpostoDTO imposto;
}
