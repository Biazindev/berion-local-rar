package com.simplificacontabil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmitirNotaRequestDTO {
    private Long vendaId;
    private Long emitenteId; // <- ID da empresa que vai emitir a nota


}
