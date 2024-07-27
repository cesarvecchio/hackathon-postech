package br.fiap.hackathonpostech.infra.mapper;

import br.fiap.hackathonpostech.application.controller.request.ClienteRequest;
import br.fiap.hackathonpostech.domain.entity.Cliente;
import br.fiap.hackathonpostech.infra.persistence.entity.ClienteEntity;

public class ClienteMapper {

    private ClienteMapper() {
    }

    public static Cliente entityToCliente(ClienteEntity clienteEntity) {
        return new Cliente(
            clienteEntity.getId(),
            clienteEntity.getCpf(),
            clienteEntity.getNome(),
            clienteEntity.getEmail(),
            clienteEntity.getTelefone(),
            clienteEntity.getRua(),
            clienteEntity.getCidade(),
            clienteEntity.getEstado(),
            clienteEntity.getCep(),
            clienteEntity.getPais()
        );
    }

    public static ClienteEntity clienteToEntity(Cliente cliente) {
        return new ClienteEntity(
            cliente.getId(),
            cliente.getCpf(),
            cliente.getNome(),
            cliente.getEmail(),
            cliente.getTelefone(),
            cliente.getRua(),
            cliente.getCidade(),
            cliente.getEstado(),
            cliente.getCep(),
            cliente.getPais()
        );
    }

    public static Cliente requestToCliente(ClienteRequest clienteRequest) {
        return new Cliente(
            null,
            clienteRequest.cpf(),
            clienteRequest.nome(),
            clienteRequest.email(),
            clienteRequest.telefone(),
            clienteRequest.rua(),
            clienteRequest.cidade(),
            clienteRequest.estado(),
            clienteRequest.cep(),
            clienteRequest.pais()
        );
    }
}
