package com.simplificacontabil.model;

import com.simplificacontabil.enums.Perfil;
import com.simplificacontabil.enums.Perfil;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true) // Garantir que o 'username' seja único
    private String username;  // Adicionei o campo 'username' que é necessário para a consulta
    private String nome;
    private String email;
    private String senha;

    @Enumerated(EnumType.STRING)
    private Perfil perfil; // ADMIN ou COMUM
}
