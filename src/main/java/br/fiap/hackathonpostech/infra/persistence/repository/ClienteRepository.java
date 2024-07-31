package br.fiap.hackathonpostech.infra.persistence.repository;

import br.fiap.hackathonpostech.infra.persistence.entity.ClienteEntity;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteEntity, UUID> {
    Optional<ClienteEntity> findByCpf(String cpf);
}
