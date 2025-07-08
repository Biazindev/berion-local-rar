package com.simplificacontabil.integracoes.shopee.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class ShopeeSigner {

    public static String generateSign(String baseString, String partnerKey) {
        try {
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(partnerKey.getBytes(), "HmacSHA256");
            hmacSha256.init(secretKey);
            byte[] hash = hmacSha256.doFinal(baseString.getBytes());
            return Base64.getEncoder().encodeToString(hash).toLowerCase(); // Shopee exige lowercase hex
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar assinatura", e);
        }
    }
}

