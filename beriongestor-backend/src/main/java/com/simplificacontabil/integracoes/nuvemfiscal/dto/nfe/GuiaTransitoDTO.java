package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import lombok.*;
import com.fasterxml.jackson.annotation.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GuiaTransitoDTO {
    private Integer tpGuia;
    private String UFGuia;
    private String serieGuia;
    private String nGuia;
}