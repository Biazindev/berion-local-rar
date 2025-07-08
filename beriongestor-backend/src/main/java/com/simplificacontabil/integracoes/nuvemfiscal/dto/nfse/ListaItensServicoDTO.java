package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListaItensServicoDTO {

    /**
     * A tag XML esperada é <ListaItensServico><ItemServico>…
     * mas em JSON usamos a chave camelCase.
     */
    @JsonProperty("itemServico")
    private List<ItemServicoDTO> itemServico;
}
