package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefECFDTO {

    @JsonProperty("mod")
    private String mod;

    @JsonProperty("nECF")
    private Integer nECF;

    @JsonProperty("nCOO")
    private Integer nCOO;
}
