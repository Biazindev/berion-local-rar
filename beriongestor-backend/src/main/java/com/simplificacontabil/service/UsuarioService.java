package com.simplificacontabil.service;


import com.simplificacontabil.dto.UsuarioDTO;
import com.simplificacontabil.mapper.UsuarioMapper;
import com.simplificacontabil.model.Usuario;
import com.simplificacontabil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class UsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioMapper usuarioMapper;

    public String criarUsuario(Usuario usuario) {
        String encodedPassword = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(encodedPassword);
        usuarioRepository.save(usuario);
        return "Usuário criado com sucesso!";
    }

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toDTO)
                .toList();
    }
    public UsuarioDTO buscarPorId (Long id){
            return usuarioRepository.findById(id)
                    .map(usuarioMapper::toDTO)
                    .orElse(null);
        }

    public Usuario getUsuarioAutenticado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return usuarioRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        }

        throw new RuntimeException("Usuário não autenticado");
    }

}