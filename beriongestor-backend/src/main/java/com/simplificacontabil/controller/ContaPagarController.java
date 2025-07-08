package com.simplificacontabil.controller;

import com.simplificacontabil.dto.ContaPagarDTO;
import com.simplificacontabil.enums.StatusContaPagar;
import com.simplificacontabil.enums.TipoMovimentacao;
import com.simplificacontabil.helper.HistoricoHelper;
import com.simplificacontabil.mapper.ContaPagarMapper;
import com.simplificacontabil.model.ContaPagar;
import com.simplificacontabil.repository.ContaPagarRepository;
import com.simplificacontabil.service.ContaPagarService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/contas-pagar")
@RequiredArgsConstructor
public class ContaPagarController {

    private final ContaPagarService contaPagarService;
    private final ContaPagarMapper contaPagarMapper;
    private final ContaPagarRepository contaPagarRepository;
    private final HistoricoHelper historicoHelper;

    @PostMapping
    public ContaPagarDTO criar(@RequestBody ContaPagarDTO dto) {
        return contaPagarService.salvar(dto);
    }

    @GetMapping
    public List<ContaPagarDTO> listar() {
        return contaPagarService.listarTodos();
    }

    @GetMapping("/{id}")
    public ContaPagarDTO buscar(@PathVariable Long id) {
        return contaPagarService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        contaPagarService.deletar(id);
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<ContaPagarDTO> pagarConta(@PathVariable Long id) {
        ContaPagar conta = contaPagarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta a pagar não encontrada"));

        conta.setPago(true);
        conta.setDataPagamento(LocalDate.now());
        conta.setStatus(StatusContaPagar.PAGO);
        contaPagarRepository.save(conta);

        historicoHelper.registrar(
                TipoMovimentacao.FINANCEIRO,
                "Conta paga ao fornecedor " + conta.getFornecedor() +
                        ". Valor: R$ " + conta.getValor(),
                "ContaPagar",
                conta.getId(),
                "admin"
        );

        return ResponseEntity.ok(contaPagarMapper.toDTO(conta));
    }

}
