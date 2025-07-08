package com.simplificacontabil.controller;

import com.simplificacontabil.dto.OrdemServicoDTO;
import com.simplificacontabil.service.OrdemServicoService;
import com.simplificacontabil.service.RelatorioPDFService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordens-servico")
@RequiredArgsConstructor
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;
    private final RelatorioPDFService relatorioPDFService;

    @PostMapping
    public OrdemServicoDTO criar(@RequestBody OrdemServicoDTO dto) {
        return ordemServicoService.salvar(dto);
    }

    @GetMapping
    public List<OrdemServicoDTO> listar() {
        return ordemServicoService.listarTodos();
    }

    @GetMapping("/{id}")
    public OrdemServicoDTO buscar(@PathVariable Long id) {
        return ordemServicoService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        ordemServicoService.deletar(id);
    }

    @GetMapping("/comprovante/{id}")
    public ResponseEntity<byte[]> gerarComprovante(@PathVariable Long id) {
        OrdemServicoDTO os = ordemServicoService.buscarPorId(id);
        byte[] pdf = relatorioPDFService.gerarComprovanteOrdemServico(os);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "comprovante-os-" + id + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }

}
