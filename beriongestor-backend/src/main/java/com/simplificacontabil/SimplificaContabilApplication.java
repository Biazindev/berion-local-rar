package com.simplificacontabil;

//import com.simplificacontabil.util.FirebirdDatabaseCreator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class SimplificaContabilApplication {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
//        FirebirdDatabaseCreator.criarBancoSeNaoExistir("C:/berion/base/berion.fdb");
        SpringApplication.run(SimplificaContabilApplication.class, args);
    }
    @Controller
    public static class ReactController {
        @RequestMapping(value = {
                "/",
                "/{path:[^\\.]*}",
                "/static/**",
                "/favicon.ico"
        })
        public String redirect() {
            return "forward:/index.html";
        }

        // Ou para melhor controle:
    /*
    @RequestMapping(value = {"/", "/{path:[^\\.]*}"})
    public String redirect() {
        return "forward:/index.html";
    }
    */
    }
}

