package com.simplificacontabil.service;

import com.simplificacontabil.dto.HistoricoMovimentacaoDTO;
import com.simplificacontabil.enums.TipoMovimentacao;
import com.simplificacontabil.mapper.HistoricoMovimentacaoMapper;
import com.simplificacontabil.model.HistoricoMovimentacao;
import com.simplificacontabil.repository.HistoricoMovimentacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoricoMovimentacaoService {

    private final HistoricoMovimentacaoRepository repository;
    private final HistoricoMovimentacaoMapper HistoricoMovimentacaoMapper;

    public HistoricoMovimentacaoDTO registrar(HistoricoMovimentacaoDTO dto) {
        dto.setDataHora(LocalDateTime.now()); // garante o horário de registro
        HistoricoMovimentacao entity = HistoricoMovimentacaoMapper.toEntity(dto);
        return HistoricoMovimentacaoMapper.toDTO(repository.save(entity));
    }

    public List<HistoricoMovimentacaoDTO> listarTodos() {
        return repository.findAll().stream()
                .map(HistoricoMovimentacaoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public HistoricoMovimentacaoDTO buscarPorId(Long id) {
        return repository.findById(id)
                .map(HistoricoMovimentacaoMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Histórico não encontrado"));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public List<HistoricoMovimentacaoDTO> filtrar(
            TipoMovimentacao tipo,
            String entidade,
            String usuario,
            LocalDateTime dataInicio,
            LocalDateTime dataFim
    ) {
        return repository.findAll().stream()
                .filter(h -> tipo == null || h.getTipoMovimentacao().equals(tipo))
                .filter(h -> entidade == null || h.getEntidade().equalsIgnoreCase(entidade))
                .filter(h -> usuario == null || h.getUsuarioResponsavel().equalsIgnoreCase(usuario))
                .filter(h -> dataInicio == null || !h.getDataHora().isBefore(dataInicio))
                .filter(h -> dataFim == null || !h.getDataHora().isAfter(dataFim))
                .map(HistoricoMovimentacaoMapper::toDTO)
                .collect(Collectors.toList());
    }

}
