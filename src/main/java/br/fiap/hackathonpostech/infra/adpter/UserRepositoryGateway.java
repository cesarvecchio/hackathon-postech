package br.fiap.hackathonpostech.infra.adpter;

import br.fiap.hackathonpostech.application.gateway.UserGateway;
import br.fiap.hackathonpostech.domain.entity.User;
import br.fiap.hackathonpostech.infra.mapper.UserMapper;
import br.fiap.hackathonpostech.infra.persistence.repository.UserRepository;

public class UserRepositoryGateway implements UserGateway {
    private final UserRepository userRepository;

    public UserRepositoryGateway(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String usuario, String senha) {
        return userRepository.findByUsuarioAndSenha(usuario, senha)
                .map(UserMapper::entityToUser)
                .orElse(null);

    }

}
