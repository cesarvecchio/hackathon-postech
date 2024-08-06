package br.fiap.hackathonpostech.application.gateway;

import br.fiap.hackathonpostech.domain.entity.Cartao;
import br.fiap.hackathonpostech.domain.entity.Cliente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartaoGateway {
    void gerarCartao(Cartao cartao, Cliente cliente);

    List<Cartao> buscarCartoesPorCpf(String cpf);

    boolean existeCartaoPorNumero(String numero);

    Optional<Cartao> buscarCartaoPorId(UUID id);
}
