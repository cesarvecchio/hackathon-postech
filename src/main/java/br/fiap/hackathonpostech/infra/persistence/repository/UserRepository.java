package br.fiap.hackathonpostech.infra.persistence.repository;

import br.fiap.hackathonpostech.infra.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsuarioAndSenha(String usuario, String senha);
}
