package br.fiap.hackathonpostech;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "Hackathon PosTech", version = "1",
    description = "API desenvolvida para o processamento de pagamentos de operadas de cartão de crédito"))
@SpringBootApplication
public class HackathonPostechApplication {

    public static void main(String[] args) {
        SpringApplication.run(HackathonPostechApplication.class, args);
    }

}
