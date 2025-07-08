package com.simplificacontabil.util;

import org.simpleframework.xml.core.Persister;
import java.io.StringWriter;

public class XmlValidatorUtil {

    /**
     * Valida qualquer objeto XML compatível com SimpleXML e os XSDs embutidos na lib da SEFAZ.
     * Útil para testar NFNota, NFNotaProcessada, eventos, etc.
     *
     * @param objeto qualquer objeto mapeado com @Root da biblioteca
     * @throws RuntimeException se o XML for inválido
     */
    public static void validar(Object objeto) {
        try {
            new Persister().write(objeto, new StringWriter());
        } catch (Exception e) {
            throw new RuntimeException("❌ Erro de validação XML: " + e.getMessage(), e);
        }
    }
}
