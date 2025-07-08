package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import lombok.*;
import com.fasterxml.jackson.annotation.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IntermediadorDTO {
    private String CNPJ;
    private String idCadIntTran;
}