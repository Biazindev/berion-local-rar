package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TribMunicipalDTO {

        @JsonProperty("tribISSQN")
        private int tribISSQN;

        @JsonProperty("cLocIncid")
        private String cLocIncid;

        @JsonProperty("cPaisResult")
        private String cPaisResult;

        @JsonProperty("BM")
        private BMDTO BM;

        @JsonProperty("exigSusp")
        private ExigibilidadeSuspensaoDTO exigSusp;

        @JsonProperty("tpImunidade")
        private Integer tpImunidade;

        @JsonProperty("vBC")
        private Double vBC;

        @JsonProperty("pAliq")
        private Double pAliq;

        @JsonProperty("vISSQN")
        private Double vISSQN;

        @JsonProperty("tpRetISSQN")
        private Integer tpRetISSQN;

        @JsonProperty("vLiq")
        private Double vLiq;
    }


