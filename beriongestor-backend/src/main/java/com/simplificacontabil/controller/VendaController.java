package com.simplificacontabil.controller;

import com.simplificacontabil.dto.*;
import com.simplificacontabil.model.Venda;
import com.simplificacontabil.repository.VendaRepository;
import com.simplificacontabil.service.VendaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/venda")
public class VendaController {


    @Autowired
    private VendaService vendaService;

    @Autowired
    private VendaRepository vendaRepository;


    @PostMapping
    public ResponseEntity<byte[]> finalizarVendas(@RequestBody VendaRequestDTO vendaDTO) {
        try {
            Venda venda = vendaService.finalizarVendaComPagamentoRequest(vendaDTO);
            byte[] pdf = vendaService.gerarReciboPDF(venda);
            return ResponseEntity.ok("Venda finalizada com sucesso!".getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("Erro ao finalizar venda: {}", e.getMessage(), e);
            return buildErrorResponse(); 
        }
    }

//    @PostMapping("/finalizar")
//    public ResponseEntity<?> finalizarVenda(@RequestBody VendaRequestDTO dto) {
//        try {
//            Venda venda = vendaService.finalizarVendaComPagamentoRequest(dto);
//            byte[] pdf = vendaService.gerarReciboPDF(venda);
//            return buildPdfResponse(pdf, venda.getId());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao finalizar venda: " + e.getMessage());
//        }
//    }


    @PostMapping("/finalizar")
    public ResponseEntity<?> finalizarVenda(@RequestBody VendaRequestDTO dto) {
        try {
            Venda venda = vendaService.finalizarVendaComPagamentoRequest(dto);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("vendaId", venda.getId());
            response.put("mensagem", "Venda finalizada com sucesso!");
            response.put("reciboUrl", "/venda/recibo/" + venda.getId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", "Erro ao finalizar venda: " + e.getMessage()));
        }
    }

    @GetMapping("/recibo/{id}")
    public ResponseEntity<byte[]> gerarReciboPorId(@PathVariable Long id) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada com ID: " + id));

        byte[] pdf = vendaService.gerarReciboPDF(venda);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=recibo-" + id + ".pdf")
                .body(pdf);
    }



    @PostMapping("/gerarRecibo")
    public ResponseEntity<byte[]> gerarRecibo(@RequestBody VendaPagamentoDTO vendaPagamentoDTO) {
        try {
            VendaDTO vendaDTO = vendaPagamentoDTO.getVenda();
            PagamentoDTO pagamentoDTO = vendaPagamentoDTO.getPagamento();

            Venda venda = vendaService.gerarVendaParaRecibo(vendaDTO, pagamentoDTO);
            byte[] pdf = vendaService.gerarReciboPDF(venda);

            return buildPdfResponse(pdf, vendaDTO.getId());
        } catch (Exception e) {
            log.error("Erro ao gerar recibo: {}", e.getMessage(), e);
            return buildErrorResponse();
        }
    }

    @GetMapping
    public ResponseEntity<List<VendaDTO>> listar() {
        try {
            return ResponseEntity.ok(vendaService.listarTodas());
        } catch (Exception e) {
            log.error("Erro ao listar vendas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ResponseEntity<byte[]> buildPdfResponse(byte[] pdf, Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=recibo-" + id + ".pdf")
                .body(pdf);
    }

    private ResponseEntity<byte[]> buildErrorResponse() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                .body("Erro ao processar a requisição. Por favor, tente novamente."
                        .getBytes(StandardCharsets.UTF_8));
    }
    @GetMapping("/total-dia")
    public ResponseEntity<List<AgrupamentoPeriodoDasVendaDTO>> getTotalDia() {
        return ResponseEntity.ok(vendaService.listarVendasPorDias());
    }

    @GetMapping("/total-semana")
    public ResponseEntity<List<AgrupamentoPeriodoDasVendaDTO>> getTotalSemana() {
        return ResponseEntity.ok(vendaService.listarVendasPorSemana());
    }

    @GetMapping("/total-mes")
    public ResponseEntity<List<AgrupamentoPeriodoDasVendaDTO>> getTotalMes() {
        return ResponseEntity.ok(vendaService.listarVendasPorMeses());
    }

    @GetMapping("/total-ano")
    public ResponseEntity<List<AgrupamentoPeriodoDasVendaDTO>> getTotalAno() {
        return ResponseEntity.ok(vendaService.listarVendasPorAnos());
    }

    @GetMapping("/vendas-por-periodo")
    public ResponseEntity<List<Venda>> getVendasPorPeriodo(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim){
        try {
            return ResponseEntity.ok(vendaService.getTodasAsVendasPorPeriodo(inicio.atStartOfDay(), fim.plusDays(1).atStartOfDay()));
        } catch (Exception e) {
            log.error("Erro ao buscar total por período: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/total-por-periodo")
    public ResponseEntity<BigDecimal> getTotalPorPeriodo(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        try {
            return ResponseEntity.ok(vendaService.getTotalPorPeriodo(inicio.atStartOfDay(), fim.plusDays(1).atStartOfDay()));
        } catch (Exception e) {
            log.error("Erro ao buscar total por período: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @GetMapping("/quantidade-dia")
    public ResponseEntity<Long> getQuantidadeVendasDoDia() {
        return ResponseEntity.ok(vendaService.getQuantidadeDoDia());
    }

    @GetMapping("/totais-semanais")
    public ResponseEntity<Map<String, BigDecimal>> getTotaisPorSemana() {
        return ResponseEntity.ok(vendaService.getTotaisPorSemanaDoAno());
    }

    @GetMapping("/totais-mensais")
    public ResponseEntity<Map<String, BigDecimal>> getTotaisPorMes() {
        return ResponseEntity.ok(vendaService.getTotaisPorMesDoAno());
    }


    @GetMapping("/totais-diario")
    public ResponseEntity<Map<String, BigDecimal>> getTotaisPorDiaDaSemana() {
        Map<DayOfWeek, BigDecimal> totais = vendaService.getTotaisDaSemanaPorDia();

        // Convertendo o enum DayOfWeek para nomes mais amigáveis (ex: "SEGUNDA-FEIRA")
        Map<String, BigDecimal> totaisFormatados = new LinkedHashMap<>();
        for (Map.Entry<DayOfWeek, BigDecimal> entry : totais.entrySet()) {
            String diaFormatado = formatarDiaSemana(entry.getKey());
            totaisFormatados.put(diaFormatado, entry.getValue());
        }

        return ResponseEntity.ok(totaisFormatados);
    }

    private String formatarDiaSemana(DayOfWeek dia) {
        switch (dia) {
            case MONDAY: return "Segunda-feira";
            case TUESDAY: return "Terça-feira";
            case WEDNESDAY: return "Quarta-feira";
            case THURSDAY: return "Quinta-feira";
            case FRIDAY: return "Sexta-feira";
            case SATURDAY: return "Sábado";
            case SUNDAY: return "Domingo";
            default: return dia.name();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Venda>> buscarPorClienteOuDocumento(@RequestParam String busca) {
        if (busca == null || busca.isBlank()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<Venda> vendas = vendaService.buscarVendasPorClienteOuDocumento(busca);

        if (vendas.isEmpty()) {
            return ResponseEntity.noContent().build(); // ou retornar 200 com lista vazia, se preferir
        }

        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/total-hoje")
    public ResponseEntity<BigDecimal> getTotalHoje() {
        return ResponseEntity.ok(vendaService.getTotalDoDia());
    }

    @GetMapping("/total-semana-atual")
    public ResponseEntity<BigDecimal> getTotalSemanaAtual() {
        return ResponseEntity.ok(vendaService.getTotalDaSemana());
    }

    @GetMapping("/total-mes-atual")
    public ResponseEntity<BigDecimal> getTotalMesAtual() {
        return ResponseEntity.ok(vendaService.getTotalDoMes());
    }

    @GetMapping("/total-ano-atual")
    public ResponseEntity<BigDecimal> getTotalAnoAtual() {
        return ResponseEntity.ok(vendaService.getTotalDoAno());
    }

    @GetMapping("/totais-anuais")
    public ResponseEntity<Map<Integer, BigDecimal>> getTotaisAnuais() {
        return ResponseEntity.ok(vendaService.getTotaisPorAnos());
    }


}

