package com.simplificacontabil.controller;

import com.simplificacontabil.model.CepResponse;
import com.simplificacontabil.service.CepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cep")
public class CepController {

    @Autowired
    private CepService cepService;

    // Endpoint para consultar o CEP
    @GetMapping("/{cep}")
    public CepResponse consultarCep(@PathVariable String cep) {
        return cepService.consultarCep(cep);
    }
}
