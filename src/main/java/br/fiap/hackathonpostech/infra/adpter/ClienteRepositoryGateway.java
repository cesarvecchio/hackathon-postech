package br.fiap.hackathonpostech.infra.adpter;

import br.fiap.hackathonpostech.application.gateway.ClienteGateway;
import br.fiap.hackathonpostech.domain.entity.Cliente;
import br.fiap.hackathonpostech.infra.mapper.ClienteMapper;
import br.fiap.hackathonpostech.infra.persistence.entity.ClienteEntity;
import br.fiap.hackathonpostech.infra.persistence.repository.ClienteRepository;

public class ClienteRepositoryGateway implements ClienteGateway {

    private final ClienteRepository clienteRepository;

    public ClienteRepositoryGateway(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente registrarCliente(Cliente cliente) {
        ClienteEntity clienteEntity = ClienteMapper.clienteToEntity(cliente);

        return ClienteMapper.entityToCliente(clienteRepository.save(clienteEntity));
    }

    @Override
    public Cliente buscarClientePorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf)
            .map(ClienteMapper::entityToCliente)
            .orElse(null);
    }
}
