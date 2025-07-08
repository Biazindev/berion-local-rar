package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfNfeSuplDTO {

    @JsonProperty("qrCode")
    private String qrCode;

    @JsonProperty("urlChave")
    private String urlChave;
}
