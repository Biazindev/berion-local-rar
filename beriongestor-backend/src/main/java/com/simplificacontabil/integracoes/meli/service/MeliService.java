package com.simplificacontabil.integracoes.meli.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.simplificacontabil.integracoes.meli.dto.PedidoMLDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MeliService {

    public List<PedidoMLDTO> parsePedidos(JsonNode root) {
        List<PedidoMLDTO> pedidos = new ArrayList<>();
        for (JsonNode order : root.path("results")) {
            PedidoMLDTO dto = new PedidoMLDTO();
            dto.setId(order.path("id").asText());
            dto.setStatus(order.path("status").asText());
            dto.setDataCriacao(LocalDateTime.parse(order.path("date_created").asText()));
            dto.setValorTotal(new BigDecimal(order.path("total_amount").asText("0")));
            dto.setNomeComprador(order.path("buyer").path("nickname").asText());
            pedidos.add(dto);
        }
        return pedidos;
    }


}
