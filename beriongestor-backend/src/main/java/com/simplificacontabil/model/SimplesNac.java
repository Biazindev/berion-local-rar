package com.simplificacontabil.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimplesNac {
    private boolean optante;
    @Nullable
    private String dataExclusao;
    private String ultima_atualizacao;
}
