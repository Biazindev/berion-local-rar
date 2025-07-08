package com.simplificacontabil.controller;

import com.simplificacontabil.dto.UsuarioDTO;
import com.simplificacontabil.model.Usuario;
import com.simplificacontabil.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody Usuario usuario) {
        try {
            usuarioService.criarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioDTO>> listar() {
        List<UsuarioDTO> todos = usuarioService.listarTodos();
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        log.info("🔍 Entrou no endpoint /usuario/{}!", id);
        UsuarioDTO usuario = usuarioService.buscarPorId(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }
}
