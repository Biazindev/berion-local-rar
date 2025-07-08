package com.simplificacontabil.controller;


import com.simplificacontabil.model.Venda;
import com.simplificacontabil.repository.PagamentoRepository;
import com.simplificacontabil.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @GetMapping
    public Map<String, Object> totalDia() {
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicio = hoje.atStartOfDay();
        LocalDateTime fim = hoje.atTime(LocalTime.MAX);

        BigDecimal totalVendasDia = vendaRepository.findByDataVendaBetween(inicio, fim)
                .stream()
                .map(Venda::getTotalVenda)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> dados = new HashMap<>();
        dados.put("totalVendasDia", totalVendasDia);

        return dados;
    }

}
