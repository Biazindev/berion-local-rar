package com.simplificacontabil.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaFallbackController {

    // Este mapeamento captura todas as requisições que não foram mapeadas
    // por outros controllers ou recursos estáticos.
    // Ele exclui explicitamente caminhos que contêm um ponto (.),
    // o que geralmente significa arquivos estáticos (ex: .js, .css, .png).
    // Isso evita que ele intercepte seus assets.
    @GetMapping(value = "/**/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/index.html";
    }

    // Opcional: Se você tiver URLs com mais de um nível de pasta sem extensão
    @GetMapping(value = "/**/{path:[^\\.]*}/**")
    public String redirectDeep() {
        return "forward:/index.html";
    }

    // Mapeia a raiz para o index.html
    @GetMapping(value = "/")
    public String redirectToRoot() {
        return "forward:/index.html";
    }
}