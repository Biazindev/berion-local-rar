package com.simplificacontabil.util;

import java.math.BigDecimal;


public class MoedaUtil {
    public static BigDecimal converterParaBigDecimal(String valor) {
        if (valor == null || valor.isBlank()) {
            return BigDecimal.ZERO;
        }

        // Remove símbolo R$ e espaços
        valor = valor.replace("R$", "").trim();

        // Substitui vírgula por ponto, se necessário
        if (valor.contains(",")) {
            valor = valor.replace(".", "").replace(",", ".");
        }

        try {
            return new BigDecimal(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor monetário inválido: " + valor);
        }
    }

}
