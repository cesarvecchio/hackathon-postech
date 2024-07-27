package br.fiap.hackathonpostech.application.gateway;

import br.fiap.hackathonpostech.domain.entity.Cliente;

public interface ClienteGateway {
    Cliente registrarCliente(Cliente cliente);
}
