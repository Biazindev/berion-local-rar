package com.simplificacontabil.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numero;
    private boolean aberta;
    private String sessao;

    @OneToMany(mappedBy = "mesa", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Pedido> pedidos = new ArrayList<>();
}
