package br.fiap.hackathonpostech.application.gateway;

import br.fiap.hackathonpostech.domain.entity.Cliente;

import java.util.UUID;

public interface ClienteGateway {
    Cliente registrarCliente(Cliente cliente);

    Cliente buscarClientePorCpf(String cpf);

    Cliente buscarClientePorId(UUID id);
}
