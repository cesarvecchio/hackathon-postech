package br.fiap.hackathonpostech.main.config;

import br.fiap.hackathonpostech.application.gateway.PagamentoGateway;
import br.fiap.hackathonpostech.application.usecase.CartaoUseCase;
import br.fiap.hackathonpostech.application.usecase.ClienteUseCase;
import br.fiap.hackathonpostech.application.usecase.PagamentoUseCase;
import br.fiap.hackathonpostech.infra.adpter.PagamentoRepositoryGateway;
import br.fiap.hackathonpostech.infra.persistence.repository.PagamentoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PagamentoConfig {

    @Bean
    PagamentoUseCase pagamentoUseCase(PagamentoGateway pagamentoGateway, CartaoUseCase cartaoUseCase, ClienteUseCase clienteUseCase) {
        return new PagamentoUseCase(pagamentoGateway, cartaoUseCase, clienteUseCase);
    }

    @Bean
    PagamentoGateway pagamentoGateway(PagamentoRepository pagamentoRepository) {
        return new PagamentoRepositoryGateway(pagamentoRepository);
    }
}
