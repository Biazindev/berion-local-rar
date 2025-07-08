package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TranspDTO {
    private Integer modFrete;          // já tinha
    private TransportaDTO transporta;  // novo bloco <transporta>
    private List<VolDTO> vol;          // novo bloco <vol>[]
}