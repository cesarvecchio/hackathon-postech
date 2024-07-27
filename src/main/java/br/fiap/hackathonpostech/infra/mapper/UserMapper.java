package br.fiap.hackathonpostech.infra.mapper;

import br.fiap.hackathonpostech.domain.entity.User;
import br.fiap.hackathonpostech.infra.persistence.entity.UserEntity;


public class UserMapper {

    private UserMapper() {
    }

    public static User entityToUser(UserEntity userEntity) {
        return new User(userEntity.getUsuario(), userEntity.getSenha());
    }

    public static UserEntity userToEntity(User user) {
        return new UserEntity(user.getUsuario(), user.getSenha());
    }
}
