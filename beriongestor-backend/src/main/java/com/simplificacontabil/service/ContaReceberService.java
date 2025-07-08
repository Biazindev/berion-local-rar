package com.simplificacontabil.service;

import com.simplificacontabil.dto.ContaReceberDTO;
import com.simplificacontabil.dto.HistoricoMovimentacaoDTO;
import com.simplificacontabil.enums.TipoMovimentacao;
import com.simplificacontabil.exception.ClienteNotFoundException;
import com.simplificacontabil.helper.HistoricoHelper;
import com.simplificacontabil.mapper.ContaReceberMapper;
import com.simplificacontabil.model.Cliente;
import com.simplificacontabil.model.ContaReceber;
import com.simplificacontabil.model.PessoaFisica;
import com.simplificacontabil.repository.ClienteRepository;
import com.simplificacontabil.repository.ContaReceberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContaReceberService {

    private final ContaReceberRepository contaReceberRepository;
    private final HistoricoHelper historicoHelper;
    private final ContaReceberMapper contaReceberMapper;

    public ContaReceberDTO salvar(ContaReceberDTO dto) {
        // Pega o nome diretamente do DTO
        String nomeCliente = dto.getCliente() != null ? dto.getCliente() : "Cliente desconhecido";

        // Cria a entidade e salva
        ContaReceber entidade = contaReceberMapper.toEntity(dto);
        ContaReceber salva = contaReceberRepository.save(entidade);

        // Registra histórico se já quitado
        if (Boolean.TRUE.equals(dto.getRecebido())) {
            historicoHelper.registrar(
                    TipoMovimentacao.FINANCEIRO,
                    "Conta a receber quitada por " + nomeCliente +
                            ". Valor: R$ " + dto.getValor(),
                    "ContaReceber",
                    salva.getId(),
                    "admin"
            );
        }

        return contaReceberMapper.toDTO(salva);
    }

    public List<ContaReceberDTO> listarTodos() {
        return contaReceberRepository.findAll()
                .stream()
                .map(contaReceberMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ContaReceberDTO buscarPorId(Long id) {
        ContaReceber conta = contaReceberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta a receber não encontrada"));
        return contaReceberMapper.toDTO(conta);
    }

    public void deletar(Long id) {
        contaReceberRepository.deleteById(id);
    }
}
