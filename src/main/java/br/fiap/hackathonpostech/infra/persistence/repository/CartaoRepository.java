package br.fiap.hackathonpostech.infra.persistence.repository;

import br.fiap.hackathonpostech.infra.persistence.entity.CartaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartaoRepository extends JpaRepository<CartaoEntity, Integer> {
    List<CartaoEntity> findAllByCpf(String cpf);

    boolean existsByNumero(String numero);
}
