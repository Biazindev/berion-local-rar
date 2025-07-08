package com.simplificacontabil.setup;

import com.simplificacontabil.enums.Perfil;
import com.simplificacontabil.enums.TipoCliente;
import com.simplificacontabil.model.ConfiguracaoEmpresa;
import com.simplificacontabil.model.Usuario;
import com.simplificacontabil.repository.ConfiguracaoEmpresaRepository;
import com.simplificacontabil.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommandLineSetupRunner implements CommandLineRunner {

    private final ConfiguracaoEmpresaRepository configRepo;
    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!isSetupMode(args)) return;

        Scanner scanner = new Scanner(System.in);

        System.out.println("🛠️  Setup Inicial do Sistema Simplifica Contábil");

        System.out.print("Nome Fantasia: ");
        String nome = scanner.nextLine();

        System.out.print("CNPJ ou CPF: ");
        String doc = scanner.nextLine();

        System.out.print("Tipo do Cliente (COMERCIO_ALIMENTACAO / CONTABILIDADE / OUTRO): ");
        String tipo = scanner.nextLine().toUpperCase();

        System.out.print("Email do admin: ");
        String email = scanner.nextLine();

        System.out.print("Senha do admin: ");
        String senha = scanner.nextLine();

        String apiKey = UUID.randomUUID().toString();

        ConfiguracaoEmpresa config = ConfiguracaoEmpresa.builder()
                .nomeFantasia(nome)
                .documento(doc)
                .tipoCliente(TipoCliente.valueOf(tipo))
                .habilitaModuloNfe(true)
                .dataCadastro(LocalDateTime.now())
                .apiKey(apiKey)
                .build();

        configRepo.save(config);

        Usuario admin = Usuario.builder()
                .email(email)
                .senha(passwordEncoder.encode(senha))
                .perfil(Perfil.valueOf("ADMIN"))
                .build();

        usuarioRepo.save(admin);

        logResultado(nome, doc, email, apiKey);

        System.out.println("✅ Setup concluído com sucesso!");
    }

    private boolean isSetupMode(String... args) {
        for (String arg : args) {
            if (arg.equalsIgnoreCase("setup")) {
                return true;
            }
        }
        return false;
    }

    private void logResultado(String nome, String doc, String email, String apiKey) {
        try (FileWriter writer = new FileWriter("/opt/biazin/setup-config.log", true)) {
            writer.write("==== SETUP SIMPLIFICA CONTÁBIL ====\n");
            writer.write("Empresa: " + nome + "\n");
            writer.write("Documento: " + doc + "\n");
            writer.write("Email Admin: " + email + "\n");
            writer.write("API KEY: " + apiKey + "\n");
            writer.write("Data: " + LocalDateTime.now() + "\n\n");
        } catch (IOException e) {
            System.err.println("❌ Erro ao salvar log: " + e.getMessage());
        }
    }
}