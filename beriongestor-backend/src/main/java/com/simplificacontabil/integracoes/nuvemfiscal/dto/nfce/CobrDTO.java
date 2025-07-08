package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CobrDTO {
    private List<DupDTO> dup;
}