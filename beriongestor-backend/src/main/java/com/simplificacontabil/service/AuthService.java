package com.simplificacontabil.service;

import com.simplificacontabil.model.Usuario;
import com.simplificacontabil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;



    // Método para verificar se o email já existe
    public boolean isEmailExistente(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    // Método para criar o usuário
    public void criarUsuario(Usuario usuario) {
        // Verifica se o email já está cadastrado
        if (isEmailExistente(usuario.getEmail())) {
            throw new RuntimeException("Email já está cadastrado!");
        }


        // Salva o usuário no banco
        usuarioRepository.save(usuario);
    }


    public Usuario validarUsuario(String email, String senha) {
        // Busca o usuário pelo email
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            if (senha.matches( usuario.getSenha())) {
            return usuario; // Retorna o usuário se a senha for válida
        } else {
            throw new RuntimeException("Senha incorreta"); // Caso a senha não seja válida
        }
    }
}
