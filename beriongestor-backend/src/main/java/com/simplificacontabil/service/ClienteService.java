package com.simplificacontabil.service;

import com.simplificacontabil.dto.ClienteDTO;
import com.simplificacontabil.enums.TipoPessoa;
import com.simplificacontabil.exception.ClienteNotFoundException;
import com.simplificacontabil.mapper.ClienteMapper;
import com.simplificacontabil.mapper.EnderecoMapper;
import com.simplificacontabil.model.Cliente;
import com.simplificacontabil.model.PessoaFisica;
import com.simplificacontabil.model.PessoaJuridica;
import com.simplificacontabil.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repo;
    private final ClienteMapper mapper;
    private final EnderecoMapper enderecoMapper;

    public List<ClienteDTO> listarTodos() {
        return repo.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public ClienteDTO buscarPorId(Long id) {
        return repo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado com ID: " + id));
    }

    public ClienteDTO criar(ClienteDTO dto) {
        Cliente entity = mapper.toEntity(dto);
        Cliente salvo = repo.save(entity);
        return mapper.toDTO(salvo);
    }

    @Transactional
    public ClienteDTO atualizar(Long id, ClienteDTO dto) {
        Cliente existente = repo.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado com ID: " + id));


        if (dto.getPessoaFisica() != null) {
            PessoaFisica pf = existente.getPessoaFisica();
            var pfDto = dto.getPessoaFisica();
            pf.setNome(pfDto.getNome());
            pf.setCpf(pfDto.getCpf());
            pf.setDataNascimento(pfDto.getDataNascimento());
            pf.setEmail(pfDto.getEmail());
            pf.setTelefone(pfDto.getTelefone());
            pf.setEndereco(enderecoMapper.toEntity(pfDto.getEndereco()));
        } else if (dto.getPessoaJuridica() != null) {
            PessoaJuridica pj = existente.getPessoaJuridica();
            var pjDto = dto.getPessoaJuridica();
            pj.setRazaoSocial(pjDto.getRazaoSocial());
            pj.setCnpj(pjDto.getCnpj());
            pj.setTelefone(pjDto.getTelefone());
            pj.setNaturezaJuridica(pjDto.getNaturezaJuridica());
            pj.setEndereco(enderecoMapper.toEntity(pjDto.getEndereco()));
            // … outros campos de PJ
        }
        return mapper.toDTO(existente);
    }

    @Transactional
    public void excluir(Long id) {
        Cliente c = repo.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado" + id));
        repo.delete(c);
    }

    public Optional<ClienteDTO> buscarPorCpf(String cpf) {
        String clean = cpf.replaceAll("\\D", "");
        return repo.findByPessoaFisica_Cpf(clean)
                .map(mapper::toDTO);
    }

    public Optional<ClienteDTO> buscarPorTelefone(String fone) {
        String foneLimpo = fone.replaceAll("\\D", "");

        return repo.findByPessoaFisica_Telefone(foneLimpo)
                .map(mapper::toDTO)
                .or(() -> repo.findByPessoaJuridica_Telefone(foneLimpo).map(mapper::toDTO));
    }

    public Optional<ClienteDTO> buscarPorCnpj(String cnpj) {
        String clean = cnpj.replaceAll("\\D", "");
        return repo.findByPessoaJuridica_Cnpj(clean)
                .map(mapper::toDTO);
    }

    /** Busca unificada por documento (CPF ou CNPJ) */
    public Optional<ClienteDTO> buscarPorDocumento(String documento) {
        String clean = documento.replaceAll("\\D", "");
        return repo.findByDocumento(clean).map(mapper::toDTO);
    }
    public Long getQuantidadeDoMes() {
        LocalDate hoje = LocalDate.now();
        return repo.countByDataCadastroBetween(
                hoje.atStartOfDay(),
                hoje.plusDays(1).atStartOfDay()
        );
    }

}
