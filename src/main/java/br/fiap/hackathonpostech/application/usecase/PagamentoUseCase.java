package br.fiap.hackathonpostech.application.usecase;

import br.fiap.hackathonpostech.application.exceptions.CartaoNaoExisteException;
import br.fiap.hackathonpostech.application.exceptions.CodigoCartaoInvalidoException;
import br.fiap.hackathonpostech.application.exceptions.LimiteQtdCartoesException;
import br.fiap.hackathonpostech.application.gateway.PagamentoGateway;
import br.fiap.hackathonpostech.domain.entity.Cartao;
import br.fiap.hackathonpostech.domain.entity.Pagamento;

import java.util.List;

public class PagamentoUseCase {
    private final PagamentoGateway pagamentoGateway;
    private final CartaoUseCase cartaoUseCase;

    public PagamentoUseCase(PagamentoGateway pagamentoGateway, CartaoUseCase cartaoUseCase) {
        this.pagamentoGateway = pagamentoGateway;
        this.cartaoUseCase = cartaoUseCase;
    }

    public Pagamento registrarPagamento(Pagamento pagamento) {

        Cartao cartao = cartaoUseCase.buscarCartoesPorCpf(pagamento.getCpf())
                .stream()
                .filter(card -> card.getNumero().equals(pagamento.getNumero()))
                .findAny()
                .orElseThrow(() -> new CartaoNaoExisteException("Não existe um cartão com esse número para esse CPF!"));

        validarCodigoCartao(cartao, pagamento.getCvv());

        return null; // TODO: IMPLEMENTAR RETORNO REAL
    }

    private void validarCodigoCartao(Cartao cartao, String cvv) {
        if(!cartao.getCvv().equals(cvv)){
            throw new CodigoCartaoInvalidoException("Código informado inválido!");
        }
    }
}
