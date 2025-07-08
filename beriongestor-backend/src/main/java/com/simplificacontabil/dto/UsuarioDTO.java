package com.simplificacontabil.dto;

import com.simplificacontabil.enums.Perfil;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UsuarioDTO {

    private Long id;
    private String username;
    private String nome;
    private String email;
    private String senha;
    private Perfil perfil; // ADMIN ou COMUM

}
