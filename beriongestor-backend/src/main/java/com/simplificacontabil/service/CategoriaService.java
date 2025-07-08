package com.simplificacontabil.service;

import com.simplificacontabil.dto.CategoriaDTO;
import com.simplificacontabil.mapper.CategoriaMapper;
import com.simplificacontabil.model.Categoria;
import com.simplificacontabil.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper CategoriaMapper;

    public CategoriaDTO salvar(CategoriaDTO dto) {
        Categoria categoria = CategoriaMapper.toEntity(dto);
        return CategoriaMapper.toDTO(categoriaRepository.save(categoria));
    }

    public List<CategoriaDTO> listarTodos() {
        return categoriaRepository.findAll()
                .stream()
                .map(CategoriaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CategoriaDTO buscarPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        return CategoriaMapper.toDTO(categoria);
    }

    public void deletar(Long id) {
        categoriaRepository.deleteById(id);
    }
}
