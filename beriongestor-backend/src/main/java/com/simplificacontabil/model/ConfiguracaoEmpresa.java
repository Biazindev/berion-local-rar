package com.simplificacontabil.model;

import com.simplificacontabil.enums.TipoCliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracaoEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeFantasia;
    private String razaoSocial;
    private String documento; // CNPJ ou CPF
    private boolean pessoaJuridica;
    private String emailContato;
    private String telefoneContato;
    private String caminhoCertificado; // ou byte[] se for armazenar no banco
    private String senhaCertificado;
    private boolean habilitaModuloNfe;
    private boolean habilitaModuloBoleto;
    private boolean habilitaModuloPix;
    private boolean habilitaControleEstoque;
    private boolean habilitaModuloMesas;
    private boolean habilitaModuloPedidos;
    private LocalDateTime dataCadastro;
    @Enumerated(EnumType.STRING)
    private TipoCliente tipoCliente;
    private String apiKey;
    private String inscricaoEstadual;
    private String regimeTributario;

}
