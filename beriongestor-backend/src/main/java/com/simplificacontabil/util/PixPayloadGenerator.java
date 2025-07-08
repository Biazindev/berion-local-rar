package com.simplificacontabil.util;

public class PixPayloadGenerator {
    private static final String ID_PAYLOAD_FORMAT_INDICATOR = "00";
    private static final String PAYLOAD_FORMAT_INDICATOR = "01";

    private static final String ID_MERCHANT_ACCOUNT_INFORMATION = "26";
    private static final String ID_GUI = "00";
    private static final String ID_KEY = "01";

    private static final String ID_MERCHANT_CATEGORY_CODE = "52";
    private static final String MERCHANT_CATEGORY_CODE = "0000";

    private static final String ID_TRANSACTION_CURRENCY = "53";
    private static final String TRANSACTION_CURRENCY = "986"; // BRL

    private static final String ID_TRANSACTION_AMOUNT = "54";
    private static final String ID_COUNTRY_CODE = "58";
    private static final String COUNTRY_CODE = "BR";

    private static final String ID_MERCHANT_NAME = "59";
    private static final String ID_MERCHANT_CITY = "60";

    private static final String ID_ADDITIONAL_DATA_FIELD_TEMPLATE = "62";
    private static final String ID_TXID = "05";

    private static final String ID_CRC16 = "63";

    public static String getPayload(String pixKey, String merchantName, String merchantCity, String txid, String amount) {
        String gui = format(ID_GUI, "br.gov.bcb.pix");
        String key = format(ID_KEY, pixKey);
        String merchantAccountInfo = format(ID_MERCHANT_ACCOUNT_INFORMATION, gui + key);

        String payload =
                format(ID_PAYLOAD_FORMAT_INDICATOR, PAYLOAD_FORMAT_INDICATOR) +
                        merchantAccountInfo +
                        format(ID_MERCHANT_CATEGORY_CODE, MERCHANT_CATEGORY_CODE) +
                        format(ID_TRANSACTION_CURRENCY, TRANSACTION_CURRENCY) +
                        format(ID_TRANSACTION_AMOUNT, amount) +
                        format(ID_COUNTRY_CODE, COUNTRY_CODE) +
                        format(ID_MERCHANT_NAME, merchantName) +
                        format(ID_MERCHANT_CITY, merchantCity) +
                        format(ID_ADDITIONAL_DATA_FIELD_TEMPLATE, format(ID_TXID, txid));

        return payload + format(ID_CRC16, getCRC16(payload + ID_CRC16 + "04"));
    }

    private static String format(String id, String value) {
        return id + String.format("%02d", value.length()) + value;
    }

    private static String getCRC16(String payload) {
        int polynomial = 0x1021;
        int crc = 0xFFFF;
        for (byte b : payload.getBytes()) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                crc = ((crc & 0x8000) != 0)
                        ? ((crc << 1) ^ polynomial)
                        : (crc << 1);
            }
        }
        crc &= 0xFFFF;
        return String.format("%04X", crc);
    }
}
