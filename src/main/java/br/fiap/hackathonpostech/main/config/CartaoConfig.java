package br.fiap.hackathonpostech.main.config;

import br.fiap.hackathonpostech.application.gateway.CartaoGateway;
import br.fiap.hackathonpostech.application.usecase.CartaoUseCase;
import br.fiap.hackathonpostech.application.usecase.ClienteUseCase;
import br.fiap.hackathonpostech.infra.adpter.CartaoRepositoryGateway;
import br.fiap.hackathonpostech.infra.persistence.repository.CartaoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CartaoConfig {

    @Bean
    CartaoUseCase cartaoUseCase(CartaoGateway cartaoGateway, ClienteUseCase clienteUseCase) {
        return new CartaoUseCase(cartaoGateway, clienteUseCase);
    }

    @Bean
    CartaoGateway cartaoGateway(CartaoRepository cartaoRepository) {
        return new CartaoRepositoryGateway(cartaoRepository);
    }
}
