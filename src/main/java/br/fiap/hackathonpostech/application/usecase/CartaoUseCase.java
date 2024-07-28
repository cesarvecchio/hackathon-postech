package br.fiap.hackathonpostech.application.usecase;

import br.fiap.hackathonpostech.application.exceptions.CartaoExisteException;
import br.fiap.hackathonpostech.application.exceptions.LimiteQtdCartoesException;
import br.fiap.hackathonpostech.application.gateway.CartaoGateway;
import br.fiap.hackathonpostech.domain.entity.Cartao;
import br.fiap.hackathonpostech.domain.entity.Cliente;

import java.util.List;

public class CartaoUseCase {
    private final CartaoGateway cartaoGateway;
    private final ClienteUseCase clienteUseCase;

    public CartaoUseCase(CartaoGateway cartaoGateway, ClienteUseCase clienteUseCase) {
        this.cartaoGateway = cartaoGateway;
        this.clienteUseCase = clienteUseCase;
    }

    public void gerarCartao(Cartao cartao) {
        Cliente cliente = clienteUseCase.buscaClientePorCpf(cartao.getCpf());

        verificarPermissaoNovoCartao(cartao.getCpf());

        validarNumeroCartao(cartao.getNumero());

        cartaoGateway.gerarCartao(cartao, cliente);
    }

    public List<Cartao> buscarCartoesPorCpf(String cpf) {
        return cartaoGateway.buscarCartoesPorCpf(cpf);
    }

    private void verificarPermissaoNovoCartao(String cpf) {
        if(buscarCartoesPorCpf(cpf).size() == 2) {
            throw new LimiteQtdCartoesException("Limite da quantide de cartoes por CPF excedido!");
        }
    }

    private void validarNumeroCartao(String numero) {
        if(cartaoGateway.existeCartaoPorNumero(numero)){
            throw new CartaoExisteException("Numero de cartão já existente.");
        }
    }
}
