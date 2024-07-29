package br.fiap.hackathonpostech.infra.adpter;

import br.fiap.hackathonpostech.application.gateway.CartaoGateway;
import br.fiap.hackathonpostech.domain.entity.Cartao;
import br.fiap.hackathonpostech.domain.entity.Cliente;
import br.fiap.hackathonpostech.infra.mapper.CartaoMapper;
import br.fiap.hackathonpostech.infra.mapper.ClienteMapper;
import br.fiap.hackathonpostech.infra.persistence.entity.CartaoEntity;
import br.fiap.hackathonpostech.infra.persistence.repository.CartaoRepository;

import java.util.List;

public class CartaoRepositoryGateway implements CartaoGateway {
    private final CartaoRepository cartaoRepository;

    public CartaoRepositoryGateway(CartaoRepository cartaoRepository) {
        this.cartaoRepository = cartaoRepository;
    }

    @Override
    public void gerarCartao(Cartao cartao, Cliente cliente) {
        CartaoEntity cartaoEntity = CartaoMapper.cartaoToEntity(cartao);
        cartaoEntity.setCliente(ClienteMapper.clienteToEntity(cliente));

        cartaoRepository.save(cartaoEntity);
    }

    @Override
    public List<Cartao> buscarCartoesPorCpf(String cpf) {

        return cartaoRepository.findAllByCpf(cpf).stream()
                .map(CartaoMapper::entityToCartao)
                .toList();
    }

    @Override
    public boolean existeCartaoPorNumero(String numero) {
        return cartaoRepository.existsByNumero(numero);
    }


}
