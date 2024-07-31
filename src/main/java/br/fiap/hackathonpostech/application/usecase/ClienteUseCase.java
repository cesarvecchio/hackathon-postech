package br.fiap.hackathonpostech.application.usecase;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import br.fiap.hackathonpostech.application.exceptions.ClienteExisteException;
import br.fiap.hackathonpostech.application.exceptions.ClienteNaoExisteException;
import br.fiap.hackathonpostech.application.gateway.ClienteGateway;
import br.fiap.hackathonpostech.domain.entity.Cliente;

import java.util.UUID;

public class ClienteUseCase {

    private final ClienteGateway clienteGateway;

    public ClienteUseCase(ClienteGateway clienteGateway) {
        this.clienteGateway = clienteGateway;
    }

    public Cliente registrarCliente(Cliente cliente) {
        validaCliente(cliente);

        return clienteGateway.registrarCliente(cliente);
    }

    public Cliente buscaClientePorCpf(String cpf) {
        Cliente clienteCadastrado = clienteGateway.buscarClientePorCpf(cpf);

        if (isNull(clienteCadastrado)) {
            throw new ClienteNaoExisteException("Nenhum cliente cadastrado com o CPF informado.");
        }

        return clienteCadastrado;
    }

    public Cliente buscaClientePorId(UUID id) {
        return clienteGateway.buscarClientePorId(id);
    }

    private void validaCliente(Cliente cliente) {
        Cliente clienteCadastrado = clienteGateway.buscarClientePorCpf(cliente.getCpf());

        if (nonNull(clienteCadastrado)) {
            throw new ClienteExisteException("Já existe um cliente com o CPF informado.");
        }
    }
}
