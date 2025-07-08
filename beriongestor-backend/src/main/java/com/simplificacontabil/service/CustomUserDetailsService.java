package com.simplificacontabil.service;

import com.simplificacontabil.model.Usuario;
import com.simplificacontabil.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioRepository repo;
    public CustomUserDetailsService(UsuarioRepository repo) { this.repo = repo; }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Usuario u = repo.findByUsername(username);
        if (u == null) throw new UsernameNotFoundException("Usuário não encontrado");
        return User
                .withUsername(u.getUsername())
                .password(u.getSenha())
                .roles(u.getPerfil().name())
                .build();
    }
}
