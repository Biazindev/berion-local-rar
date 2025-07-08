package com.simplificacontabil.config.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.simplificacontabil.util.MoedaUtil;

import java.io.IOException;
import java.math.BigDecimal;

public class MoedaDeserializer extends JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String valor = p.getText();
        return MoedaUtil.converterParaBigDecimal(valor);
    }
}

