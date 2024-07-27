package br.fiap.hackathonpostech.application.usecase;

import br.fiap.hackathonpostech.application.gateway.UserGateway;
import br.fiap.hackathonpostech.domain.entity.User;

public class UserUseCase {
    private final UserGateway userGateway;

    public UserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User login(String username, String senha) {
        //todo: implementar regras de negocio
        return userGateway.login(username, senha);
    }
}
