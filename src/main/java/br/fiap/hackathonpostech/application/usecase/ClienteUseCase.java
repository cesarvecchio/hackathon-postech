package br.fiap.hackathonpostech.application.usecase;

import br.fiap.hackathonpostech.application.gateway.ClienteGateway;
import br.fiap.hackathonpostech.domain.entity.Cliente;

public class ClienteUseCase {

    private final ClienteGateway clienteGateway;

    public ClienteUseCase(ClienteGateway clienteGateway) {
        this.clienteGateway = clienteGateway;
    }

    public Cliente registrarCliente(Cliente cliente) {
        return clienteGateway.registrarCliente(cliente);
    }
}
