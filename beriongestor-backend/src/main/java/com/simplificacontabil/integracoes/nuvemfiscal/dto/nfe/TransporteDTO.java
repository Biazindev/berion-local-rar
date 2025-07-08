package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import com.simplificacontabil.model.Transporta;
import com.simplificacontabil.model.Volume;
import lombok.*;
import com.fasterxml.jackson.annotation.*;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransporteDTO {
    @JsonProperty("modFrete")
    private Integer modFrete;
    private Transporta transporta;      // novo
    private List<Volume> vol;           // novo
}
