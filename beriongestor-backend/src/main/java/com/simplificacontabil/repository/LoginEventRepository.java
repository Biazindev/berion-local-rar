package com.simplificacontabil.repository;
import com.simplificacontabil.model.LoginEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginEventRepository extends JpaRepository<LoginEvent, Long> {
}
