package br.fiap.hackathonpostech.infra.persistence.repository;

import br.fiap.hackathonpostech.infra.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Integer> {
}
