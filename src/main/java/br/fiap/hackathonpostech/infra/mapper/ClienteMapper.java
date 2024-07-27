package br.fiap.hackathonpostech.infra.mapper;

import br.fiap.hackathonpostech.application.controller.request.ClienteRequest;
import br.fiap.hackathonpostech.domain.entity.Cliente;
import br.fiap.hackathonpostech.infra.persistence.entity.ClienteEntity;

public class ClienteMapper {

    private ClienteMapper() {
    }

    public static Cliente entityToCliente(ClienteEntity clienteEntity) {
        return Cliente.builder()
            .id(clienteEntity.getId())
            .cpf(clienteEntity.getCpf())
            .nome(clienteEntity.getNome())
            .email(clienteEntity.getEmail())
            .telefone(clienteEntity.getTelefone())
            .rua(clienteEntity.getRua())
            .cidade(clienteEntity.getCidade())
            .estado(clienteEntity.getEstado())
            .cep(clienteEntity.getCep())
            .pais(clienteEntity.getPais())
            .build();
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
        return Cliente.builder()
            .cpf(clienteRequest.cpf())
            .nome(clienteRequest.nome())
            .email(clienteRequest.email())
            .telefone(clienteRequest.telefone())
            .rua(clienteRequest.rua())
            .cidade(clienteRequest.cidade())
            .estado(clienteRequest.estado())
            .cep(clienteRequest.cep())
            .pais(clienteRequest.pais())
            .build();
    }
}
