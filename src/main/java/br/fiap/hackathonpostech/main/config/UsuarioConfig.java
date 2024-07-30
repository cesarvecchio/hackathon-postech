package br.fiap.hackathonpostech.main.config;

import br.fiap.hackathonpostech.application.gateway.UsuarioGateway;
import br.fiap.hackathonpostech.application.usecase.UsuarioUseCase;
import br.fiap.hackathonpostech.infra.adpter.UsuarioRepositoryGateway;
import br.fiap.hackathonpostech.infra.persistence.repository.UsuarioRepository;
import br.fiap.hackathonpostech.main.security.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
public class UsuarioConfig {
    @Bean
    UsuarioUseCase userUseCase(UsuarioGateway usuarioGateway, AuthenticationManager authenticationManager, TokenService tokenService) {
        return new UsuarioUseCase(usuarioGateway, authenticationManager, tokenService);
    }

    @Bean
    UsuarioGateway userGateway(UsuarioRepository usuarioRepository) {
        return new UsuarioRepositoryGateway(usuarioRepository);
    }
}
