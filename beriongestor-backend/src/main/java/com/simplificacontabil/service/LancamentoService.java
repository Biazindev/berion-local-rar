package com.simplificacontabil.service;

import com.simplificacontabil.model.Lancamento;
import com.simplificacontabil.repository.LancamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LancamentoService {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    public Lancamento salvar(Lancamento lancamento) {
        return lancamentoRepository.save(lancamento);
    }

    public List<Lancamento> buscarTodos() {
        return lancamentoRepository.findAll();
    }

    public void excluir(Long id) {
        lancamentoRepository.deleteById(id);
    }
}
