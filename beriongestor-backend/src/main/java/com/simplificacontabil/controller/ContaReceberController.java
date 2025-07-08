package com.simplificacontabil.controller;

import com.simplificacontabil.dto.ContaReceberDTO;
import com.simplificacontabil.enums.TipoMovimentacao;
import com.simplificacontabil.helper.HistoricoHelper;
import com.simplificacontabil.mapper.ContaReceberMapper;
import com.simplificacontabil.model.ContaReceber;
import com.simplificacontabil.repository.ContaReceberRepository;
import com.simplificacontabil.service.ContaReceberService;
import com.simplificacontabil.service.RelatorioPDFService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/contas-receber")
@RequiredArgsConstructor
public class ContaReceberController {

    private final ContaReceberService contaReceberService;
    private final RelatorioPDFService relatorioPDFService;
    private final ContaReceberRepository contaReceberRepository;
    private final ContaReceberMapper contaReceberMapper;
    private final HistoricoHelper historicoHelper;


    @PostMapping
    public ContaReceberDTO criar(@RequestBody ContaReceberDTO dto) {
        return contaReceberService.salvar(dto);
    }

    @GetMapping
    public List<ContaReceberDTO> listar() {
        return contaReceberService.listarTodos();
    }

    @GetMapping("/{id}")
    public ContaReceberDTO buscar(@PathVariable Long id) {
        return contaReceberService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        contaReceberService.deletar(id);
    }

    @GetMapping("/comprovante/{id}")
    public ResponseEntity<byte[]> gerarComprovante(@PathVariable Long id) {
        ContaReceberDTO conta = contaReceberService.buscarPorId(id);
        byte[] pdf = relatorioPDFService.gerarComprovanteContaReceber(conta);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "comprovante-conta-" + id + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }
    @PutMapping("/{id}/receber")
    public ResponseEntity<ContaReceberDTO> receberConta(@PathVariable Long id) {
        ContaReceber conta = contaReceberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta a receber não encontrada"));

        conta.setRecebido(true);
        conta.setDataRecebimento(LocalDate.now());
        contaReceberRepository.save(conta);

        historicoHelper.registrar(
                TipoMovimentacao.FINANCEIRO,
                "Conta a receber quitada por " + conta.getCliente() +
                        ". Valor: R$ " + conta.getValor(),
                "ContaReceber",
                conta.getId(),
                "admin"
        );

        return ResponseEntity.ok(contaReceberMapper.toDTO(conta));
    }


}
