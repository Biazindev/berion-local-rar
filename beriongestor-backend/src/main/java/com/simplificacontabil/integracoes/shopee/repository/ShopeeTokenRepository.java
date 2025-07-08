package com.simplificacontabil.integracoes.shopee.repository;


import com.simplificacontabil.integracoes.shopee.model.ShopeeToken;
import com.simplificacontabil.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopeeTokenRepository extends JpaRepository<ShopeeToken, Long> {

    Optional<ShopeeToken> findByUsuario(Usuario usuario);

    Optional<ShopeeToken> findByShopId(String shopId);
}