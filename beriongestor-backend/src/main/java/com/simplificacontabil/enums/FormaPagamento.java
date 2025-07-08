package com.simplificacontabil.enums;

public enum FormaPagamento {

    CARTAO_CREDITO("Cartão de Crédito", "03"),
    CARTAO_DEBITO("Cartão de Débito", "04"),
    PIX("Pix", "17"),
    DINHEIRO("Dinheiro", "01"),
    PARCELADO_LOJA("Parcelado na Loja", "99"),
    CARTAO("CARTÃO", "10");// Exemplo genérico

    private final String descricao;
    private final String codigoFiscal;

    FormaPagamento(String descricao, String codigoFiscal) {
        this.descricao = descricao;
        this.codigoFiscal = codigoFiscal;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCodigoFiscal() {
        return codigoFiscal;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public static FormaPagamento fromDescricao(String descricao) {
        for (FormaPagamento formaPagamento : values()) {
            if (formaPagamento.getDescricao().equalsIgnoreCase(descricao)) {
                return formaPagamento;
            }
        }
        throw new IllegalArgumentException("Forma de pagamento não encontrada: " + descricao);
    }
}
