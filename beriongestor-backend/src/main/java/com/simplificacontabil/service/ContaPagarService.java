package com.simplificacontabil.service;

import com.simplificacontabil.dto.ContaPagarDTO;
import com.simplificacontabil.enums.StatusContaPagar;
import com.simplificacontabil.enums.TipoMovimentacao;
import com.simplificacontabil.helper.HistoricoHelper;
import com.simplificacontabil.mapper.ContaPagarMapper;
import com.simplificacontabil.model.*;
import com.simplificacontabil.repository.ContaPagarRepository;
import com.simplificacontabil.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContaPagarService {

    private final ContaPagarRepository contaPagarRepository;
    private final HistoricoHelper historicoHelper;
    private final FornecedorRepository fornecedorRepository;
    private final ContaPagarMapper  contaPagarMapper;


    public ContaPagarDTO salvar(ContaPagarDTO dto) {
        ContaPagar conta = contaPagarMapper.toEntity(dto);

        LocalDate hoje = LocalDate.now();

        if (Boolean.TRUE.equals(dto.getPago())) {
            conta.setStatus(StatusContaPagar.PAGO);
            conta.setDataPagamento(hoje);
            conta.setPago(true);
        } else {
            if (dto.getVencimento().isBefore(hoje)) {
                conta.setStatus(StatusContaPagar.VENCIDA);
            } else {
                conta.setStatus(StatusContaPagar.A_VENCER);
            }
            conta.setPago(false);
        }

        ContaPagar salva = contaPagarRepository.save(conta);

        if (Boolean.TRUE.equals(dto.getPago())) {
            historicoHelper.registrar(
                    TipoMovimentacao.FINANCEIRO,
                    "Conta paga ao fornecedor " + conta.getFornecedor() +
                            ". Valor: R$ " + conta.getValor(),
                    "ContaPagar",
                    conta.getId(),
                    "admin"
            );
        }

        return contaPagarMapper.toDTO(salva);
    }



    public List<ContaPagarDTO> listarTodos() {
        return contaPagarRepository.findAll()
                .stream()
                .map(contaPagarMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ContaPagarDTO buscarPorId(Long id) {
        ContaPagar conta = contaPagarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta a pagar não encontrada"));
        return contaPagarMapper.toDTO(conta);
    }

    public void deletar(Long id) {
        contaPagarRepository.deleteById(id);
    }
}
