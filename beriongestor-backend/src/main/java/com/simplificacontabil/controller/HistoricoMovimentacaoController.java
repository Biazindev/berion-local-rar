package com.simplificacontabil.controller;

import com.simplificacontabil.dto.HistoricoMovimentacaoDTO;
import com.simplificacontabil.enums.TipoMovimentacao;
import com.simplificacontabil.service.HistoricoMovimentacaoService;
import com.simplificacontabil.service.RelatorioPDFService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/historico-movimentacoes")
@RequiredArgsConstructor
public class HistoricoMovimentacaoController {

    private final HistoricoMovimentacaoService service;
    private final RelatorioPDFService relatorioPDFService;


    @PostMapping
    public HistoricoMovimentacaoDTO registrar(@RequestBody HistoricoMovimentacaoDTO dto) {
        return service.registrar(dto);
    }

    @GetMapping
    public List<HistoricoMovimentacaoDTO> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public HistoricoMovimentacaoDTO buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    @GetMapping("/filtro")
    public List<HistoricoMovimentacaoDTO> filtrar(
            @RequestParam(required = false) TipoMovimentacao tipo,
            @RequestParam(required = false) String entidade,
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim
    ) {
        return service.filtrar(tipo, entidade, usuario, dataInicio, dataFim);
    }
    @GetMapping("/relatorio/download")
    public ResponseEntity<byte[]> downloadRelatorioHistorico(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim
    ) {
        List<HistoricoMovimentacaoDTO> dados = service.filtrar(null, null, null, dataInicio, dataFim);
        byte[] pdf = relatorioPDFService.gerarRelatorioHistorico(dados);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio-historico.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }


}
