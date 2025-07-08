package com.simplificacontabil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IpBloqueadoDTO {
    private String ip;
    private long tempoRestanteMs;
}
