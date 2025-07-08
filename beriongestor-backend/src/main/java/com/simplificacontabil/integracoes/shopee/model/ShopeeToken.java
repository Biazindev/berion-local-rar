package com.simplificacontabil.integracoes.shopee.model;

import com.simplificacontabil.model.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopeeToken {
    @Id
    @GeneratedValue
    private Long id;

    private String shopId;
    private String shopName;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expiresAt;

    @ManyToOne
    private Usuario usuario;
}
