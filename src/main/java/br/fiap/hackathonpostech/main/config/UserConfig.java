package br.fiap.hackathonpostech.main.config;

import br.fiap.hackathonpostech.application.gateway.UserGateway;
import br.fiap.hackathonpostech.application.usecase.UserUseCase;
import br.fiap.hackathonpostech.infra.adpter.UserRepositoryGateway;
import br.fiap.hackathonpostech.infra.persistence.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {
    @Bean
    UserUseCase userUseCase(UserGateway userGateway) {
        return new UserUseCase(userGateway);
    }

    @Bean
    UserGateway userGateway(UserRepository userRepository) {
        return new UserRepositoryGateway(userRepository);
    }
}
