package com.simplificacontabil.service;

import com.simplificacontabil.dto.OrdemServicoDTO;
import com.simplificacontabil.helper.HistoricoHelper;
import com.simplificacontabil.mapper.OrdemServicoMapper;
import com.simplificacontabil.model.OrdemServico;
import com.simplificacontabil.repository.ClienteRepository;
import com.simplificacontabil.repository.OrdemServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdemServicoService {

    private final OrdemServicoRepository ordemServicoRepository;
    private final  OrdemServicoMapper mapper;
    private final ClienteRepository clienteRepo;
    private final OrdemServicoRepository ordemRepo;



    public OrdemServicoDTO salvar(OrdemServicoDTO dto) {
        var cliente = clienteRepo.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        OrdemServico os = mapper.toEntity(dto, cliente);
        OrdemServico salva = ordemRepo.save(os);
        return mapper.toDTO(salva);
    }


    public List<OrdemServicoDTO> listarTodos() {
        return ordemServicoRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public OrdemServicoDTO buscarPorId(Long id) {
        OrdemServico os = ordemServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de serviço não encontrada"));
        return mapper.toDTO(os);
    }

    public void deletar(Long id) {
        ordemServicoRepository.deleteById(id);
    }
}
