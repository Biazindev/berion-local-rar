package com.simplificacontabil.integracoes.shopee.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.simplificacontabil.integracoes.shopee.model.ShopeeToken;
import com.simplificacontabil.integracoes.shopee.util.ShopeeSigner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopeeService {
    private final RestTemplate restTemplate;

    @Value("${partner-id}")
    private String partnerId;

    @Value("${partner-key}")
    private String partnerKey;

    public List<String> buscarPedidos(ShopeeToken token) {
        long timestamp = System.currentTimeMillis() / 1000;
        String path = "/api/v2/order/get_order_list";
        String baseString = partnerId + path + timestamp + token.getShopId();
        String sign = ShopeeSigner.generateSign(baseString, partnerKey);

        String url = "https://partner.shopeemobile.com" + path +
                "?partner_id=" + partnerId +
                "&timestamp=" + timestamp +
                "&access_token=" + token.getAccessToken() +
                "&shop_id=" + token.getShopId() +
                "&sign=" + sign;

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);

        List<String> pedidos = new ArrayList<>();
        assert response.getBody() != null;
        for (JsonNode order : response.getBody().path("response").path("order_list")) {
            pedidos.add(order.path("order_sn").asText());
        }
        return pedidos;
    }

}
