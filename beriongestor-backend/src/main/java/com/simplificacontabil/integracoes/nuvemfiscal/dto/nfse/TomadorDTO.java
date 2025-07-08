package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

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
public class TomadorDTO {

        @JsonProperty("orgaoPublico")
        private Boolean orgaoPublico;

        @JsonProperty("CNPJ")
        private String CNPJ;

        @JsonProperty("CPF")
        private String CPF;

        @JsonProperty("NIF")
        private String NIF;

        @JsonProperty("cNaoNIF")
        private Integer cNaoNIF;

        @JsonProperty("CAEPF")
        private String CAEPF;

        @JsonProperty("IM")
        private String IM;

        @JsonProperty("IE")
        private String IE;

        @JsonProperty("xNome")
        private String xNome;

        @JsonProperty("end")
        private EnderecoCompleto end;

        @JsonProperty("fone")
        private String fone;

        @JsonProperty("email")
        private String email;
}


