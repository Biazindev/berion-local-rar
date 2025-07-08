package com.simplificacontabil.config;

import com.fincatto.documentofiscal.DFAmbiente;
import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.nfe.NFeConfig;
import com.fincatto.documentofiscal.nfe.NFTipoEmissao;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

public class NFeConfigImpl extends NFeConfig {

    private final String caminhoCertificado;
    private final String senhaCertificado;

    public NFeConfigImpl(String caminhoCertificado, String senhaCertificado) {
        this.caminhoCertificado = caminhoCertificado;
        this.senhaCertificado = senhaCertificado;
    }

    @Override
    public KeyStore getCertificadoKeyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (InputStream is = new FileInputStream(caminhoCertificado)) {
                keyStore.load(is, senhaCertificado.toCharArray());
            }
            return keyStore;
        } catch (Exception e) {
            throw new RuntimeException("❌ Erro ao carregar o certificado externo: " + e.getMessage(), e);
        }
    }

    @Override
    public String getCadeiaCertificadosSenha() {
        return ""; // ou a senha da sua cadeia, se estiver usando .jks
    }


    @Override
    public KeyStore getCadeiaCertificadosKeyStore() {
        return null;
    }
    @Override
    public String getCertificadoSenha() {
        return "123456";
    }

    @Override
    public DFUnidadeFederativa getCUF() {
        return DFUnidadeFederativa.PR;
    }

    @Override
    public DFAmbiente getAmbiente() {
        return DFAmbiente.HOMOLOGACAO; // ou PRODUCAO
    }

    @Override
    public NFTipoEmissao getTipoEmissao() {
        return NFTipoEmissao.EMISSAO_NORMAL;
    }

    @Override
    public java.util.TimeZone getTimeZone() {
        return java.util.TimeZone.getTimeZone("America/Sao_Paulo");
    }
}
