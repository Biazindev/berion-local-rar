package com.simplificacontabil.controller;

import com.simplificacontabil.dto.ItemMesaDTO;
import com.simplificacontabil.dto.PedidoMesaDTO;
import com.simplificacontabil.enums.StatusPedido;
import com.simplificacontabil.model.Mesa;
import com.simplificacontabil.model.Pedido;
import com.simplificacontabil.repository.MesaRepository;
import com.simplificacontabil.repository.PedidoRepository;
import com.simplificacontabil.service.MesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/mesas")
@RequiredArgsConstructor
public class MesaController {

    private final MesaService mesaService;
    private final PedidoRepository pedidoRepository;
    private final MesaRepository mesaRepository;

    /**
     * Adiciona um pedido a uma mesa específica (cria a mesa se necessário).
     */
    @PostMapping("/pedido")
    public ResponseEntity<String> adicionarPedido(@RequestBody PedidoMesaDTO dto) {
        mesaService.adicionarPedidoNaMesa(dto);
        return ResponseEntity.ok("Pedido adicionado com sucesso à mesa " + dto.getNumeroMesa());
    }

    /**
     * Lista todas as mesas que ainda estão abertas.
     */
    @GetMapping("/ativas")
    public ResponseEntity<List<Mesa>> listarMesasAbertas() {
        List<Mesa> mesas = mesaService.listarMesasAbertas();
        return ResponseEntity.ok(mesas);
    }

    /**
     * Calcula o total da conta da mesa (sem fechar ou gerar PDF).
     */
    @GetMapping("/{numeroMesa}/total")
    public ResponseEntity<BigDecimal> calcularTotalMesa(@PathVariable Integer numeroMesa) {
        BigDecimal total = mesaService.calcularTotalMesa(numeroMesa);
        return ResponseEntity.ok(total);
    }

    /**
     * Finaliza a conta da mesa: marca como paga, fecha e retorna o cupom fiscal em PDF.
     */
    @PostMapping("/{numeroMesa}/finalizar")
    public ResponseEntity<byte[]> finalizarMesa(@PathVariable Integer numeroMesa) {
        byte[] pdf = mesaService.finalizarPagamentoEMesa(numeroMesa);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cupom-mesa-" + numeroMesa + ".pdf")
                .body(pdf);
    }
    @GetMapping("/{numeroMesa}/itens")
    public ResponseEntity<List<ItemMesaDTO>> visualizarItensDaMesa(@PathVariable Integer numeroMesa) {
        List<ItemMesaDTO> itens = mesaService.listarItensDaMesa(numeroMesa);
        return ResponseEntity.ok(itens);
    }
    @GetMapping
    public List<Pedido> listarPedidos(
            @RequestParam(required = false) StatusPedido status,
            @RequestParam(required = false) Long mesaId) {

        if (status != null && mesaId != null) {
            return pedidoRepository.findByStatusAndMesaId(status, mesaId);
        } else if (status != null) {
            return pedidoRepository.findByStatus(status);
        } else if (mesaId != null) {
            return pedidoRepository.findByMesaId(mesaId);
        } else {
            return pedidoRepository.findAll();
        }
    }
    /**
     * Cadastra uma nova mesa.
     */
    @PostMapping("/criar-ou-reutilizar")
    public ResponseEntity<Mesa> criarOuReutilizarMesa(@RequestParam Integer numero) {
        // Busca todas as mesas com esse número, ordenadas pela mais recente
        List<Mesa> mesas = mesaRepository.findAllByNumeroOrderByIdDesc(numero);

        // Tenta reutilizar a mais recente se estiver fechada
        if (!mesas.isEmpty()) {
            Mesa mesaMaisRecente = mesas.get(0);

            if (mesaMaisRecente.isAberta()) {
                // Já tem uma mesa aberta, retorna ela
                return ResponseEntity.ok(mesaMaisRecente);
            } else {
                // Reabre a mesa mais recente que estava fechada
                mesaMaisRecente.setAberta(true);
                mesaRepository.save(mesaMaisRecente);
                return ResponseEntity.ok(mesaMaisRecente);
            }
        }

        // Nenhuma mesa com esse número ainda, cria uma nova
        Mesa novaMesa = Mesa.builder()
                .numero(numero)
                .aberta(true)
                .build();

        mesaRepository.save(novaMesa);
        return ResponseEntity.ok(novaMesa);
    }
    @DeleteMapping("/limpar/{numeroMesa}")
    public ResponseEntity<String> limparItensMesa(@PathVariable Integer numeroMesa) {
        mesaService.limparItensDaMesa(numeroMesa);
        return ResponseEntity.ok("Todos os pedidos da mesa " + numeroMesa + " foram removidos.");
    }



}
