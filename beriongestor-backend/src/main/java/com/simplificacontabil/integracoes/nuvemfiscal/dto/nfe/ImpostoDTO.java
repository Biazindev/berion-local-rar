package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import lombok.*;
import com.fasterxml.jackson.annotation.*;
import java.util.Map;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImpostoDTO {
    private Double vTotTrib;

    @JsonProperty("ICMS")
    private Map<String, Object> ICMS;

    @JsonProperty("PIS")
    private Map<String, Object> PIS;

    @JsonProperty("COFINS")
    private Map<String, Object> COFINS;
}
