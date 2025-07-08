package com.simplificacontabil.service;

import com.simplificacontabil.dto.EmpresaDTO;
import com.simplificacontabil.mapper.EmpresaMapper;
import com.simplificacontabil.model.Empresa;
import com.simplificacontabil.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository repository;
    private final EmpresaMapper mapper;

    public EmpresaDTO salvar(EmpresaDTO dto) {
        Empresa empresa = mapper.toEntity(dto);
        return mapper.toDto(repository.save(empresa));
    }

    public EmpresaDTO buscarPorId(Long id) {
        Empresa empresa = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));
        return mapper.toDto(empresa);
    }

    public List<EmpresaDTO> listar() {
        return mapper.toDtoList(repository.findAll());
    }

    public EmpresaDTO atualizar(Long id, EmpresaDTO dto) {
        Empresa empresa = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));

        empresa.setRazaoSocial(dto.getRazaoSocial());
        empresa.setNomeFantasia(dto.getNomeFantasia());
        empresa.setCnpj(dto.getCnpj());
        empresa.setInscricaoEstadual(dto.getInscricaoEstadual());
        empresa.setInscricaoMunicipal(dto.getInscricaoMunicipal());
        empresa.setRegimeTributario(dto.getRegimeTributario());
        empresa.setCnae(dto.getCnae());
        empresa.setCrt(dto.getCrt());
        empresa.setFone(dto.getFone());
        empresa.setEndereco(dto.getEndereco());

        return mapper.toDto(repository.save(empresa));
    }

    public void deletar(Long id) {
        Empresa empresa = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));
        repository.delete(empresa);
    }
}
