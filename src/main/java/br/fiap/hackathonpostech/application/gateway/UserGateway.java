package br.fiap.hackathonpostech.application.gateway;

import br.fiap.hackathonpostech.domain.entity.User;

public interface UserGateway {

    User login(String usuario, String senha);
}
