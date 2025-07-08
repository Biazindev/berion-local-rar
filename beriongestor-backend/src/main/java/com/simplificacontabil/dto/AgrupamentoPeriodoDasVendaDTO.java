package com.simplificacontabil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrupamentoPeriodoDasVendaDTO {
    private String label;        // Ex: "Segunda-feira", "Semana 20", "Abril", "2025"
    private Map<String, BigDecimal> totais = new HashMap<>();
}
