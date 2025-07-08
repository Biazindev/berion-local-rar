package com.simplificacontabil.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.time.Instant;

@Data

@Entity
public class PasswordResetToken {
    @Id
    @GeneratedValue
    private Long id;

    private String token;

    @OneToOne
    private Usuario usuario;

    private Instant expiryDate;


}
