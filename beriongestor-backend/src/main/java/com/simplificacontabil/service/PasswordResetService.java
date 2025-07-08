package com.simplificacontabil.service;

import com.simplificacontabil.model.PasswordResetToken;
import com.simplificacontabil.model.Usuario;
import com.simplificacontabil.repository.PasswordResetTokenRepository;
import com.simplificacontabil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository repo;
    private final UsuarioRepository usuarioRepo;
    private final JavaMailSender mailSender;
    private final PasswordEncoder encoder;
    private final String resetUrl;

    public PasswordResetService(
            PasswordResetTokenRepository repo,
            UsuarioRepository usuarioRepo,
            JavaMailSender mailSender,
            PasswordEncoder encoder,
            @Value("${app.front.reset-password-url:http://localhost:3000/resetar-senha}") String resetUrl
    ) {
        this.repo = repo;
        this.usuarioRepo = usuarioRepo;
        this.mailSender = mailSender;
        this.encoder = encoder;
        this.resetUrl = resetUrl;
    }

    @Transactional
    public void createAndSendToken(String input) {
        // Tenta encontrar pelo e-mail ou username
        Usuario u = usuarioRepo.findByEmail(input)
                .orElseGet(() -> usuarioRepo.findByUsername(input));


        if (u.getEmail() == null || u.getEmail().isBlank()) {
            throw new RuntimeException("Usuário não possui e-mail cadastrado.");
        }

        // Apaga tokens antigos
        repo.deleteByUsuario(u);

        // Gera e salva o novo token
        String token = UUID.randomUUID().toString();
        PasswordResetToken prt = new PasswordResetToken();
        prt.setUsuario(u);
        prt.setToken(token);
        prt.setExpiryDate(Instant.now().plus(30, ChronoUnit.MINUTES));
        repo.save(prt);

        // Monta e envia o e-mail
        String link = resetUrl + "?token=" + token;
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(((JavaMailSenderImpl) mailSender).getUsername());
        msg.setTo(u.getEmail());
        msg.setSubject("Recuperação de senha");
        msg.setText("Clique aqui para redefinir sua senha:\n\n" + link + "\n\nEste link expira em 30 minutos.");
        mailSender.send(msg);
    }

    @Transactional  // <— também precisa transação para salvar/invalidate
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken prt = repo.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (prt.getExpiryDate().isBefore(Instant.now())) {
            repo.delete(prt);
            throw new IllegalArgumentException("Token expirado");
        }

        Usuario u = prt.getUsuario();
        u.setSenha(encoder.encode(newPassword));
        usuarioRepo.save(u);
        repo.delete(prt);
    }
}
