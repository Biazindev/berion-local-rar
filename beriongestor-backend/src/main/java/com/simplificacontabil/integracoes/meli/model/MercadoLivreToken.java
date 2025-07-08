package com.simplificacontabil.integracoes.meli.model;

import com.simplificacontabil.model.Usuario;
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
public class MercadoLivreToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mlUserId;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expiresAt;

    @ManyToOne
    private Usuario usuarioDoERP;
}
