package br.fiap.hackathonpostech.application.usecase;

import br.fiap.hackathonpostech.application.exceptions.CartaoNaoExisteException;
import br.fiap.hackathonpostech.application.exceptions.ClienteNaoExisteException;
import br.fiap.hackathonpostech.application.exceptions.LimiteExcedidoCartaoException;
import br.fiap.hackathonpostech.application.exceptions.PagamentoNaoEncontradoException;
import br.fiap.hackathonpostech.application.gateway.PagamentoGateway;
import br.fiap.hackathonpostech.domain.entity.Cartao;
import br.fiap.hackathonpostech.domain.entity.Cliente;
import br.fiap.hackathonpostech.domain.entity.Pagamento;

import java.util.List;
import java.util.UUID;

import static br.fiap.hackathonpostech.domain.enums.MetodoPagamentoEnum.CARTAO_CREDITO;
import static br.fiap.hackathonpostech.domain.enums.StatusEnum.APROVADO;
import static br.fiap.hackathonpostech.domain.enums.StatusEnum.REJEITADO;
import static java.util.Objects.isNull;

public class PagamentoUseCase {
    private final PagamentoGateway pagamentoGateway;
    private final CartaoUseCase cartaoUseCase;
    private final ClienteUseCase clienteUseCase;

    public PagamentoUseCase(PagamentoGateway pagamentoGateway, CartaoUseCase cartaoUseCase, ClienteUseCase clienteUseCase) {
        this.pagamentoGateway = pagamentoGateway;
        this.cartaoUseCase = cartaoUseCase;
        this.clienteUseCase = clienteUseCase;
    }

    public Pagamento registrarPagamento(Pagamento pagamento) {
        pagamento.setMetodoPagamento(CARTAO_CREDITO);
        pagamento.setDescricao("Compra produdo X");
        Cartao cartao = cartaoUseCase.buscarCartoesPorCpf(pagamento.getCpf())
                .stream()
                .filter(card -> card.getNumero().equals(pagamento.getNumero()))
                .findAny()
                .orElseThrow(() -> new CartaoNaoExisteException("Não existe um cartão com esse número para esse CPF!"));

        pagamento.validarCodigoCartao(cartao);
        pagamento.validarDataExpiracao(cartao);
        validarLimiteDisponivel(cartao, pagamento);

        pagamento.setStatus(APROVADO);
        Double novoLimite = cartao.getLimite() - pagamento.getValor();
        cartaoUseCase.atualizarLimiteCartao(cartao, novoLimite);

        return pagamentoGateway.registrarPagamento(pagamento, cartao);
    }

    private void validarLimiteDisponivel(Cartao cartao, Pagamento pagamento) {

        if (cartao.getLimite() <= pagamento.getValor()) {
            pagamento.setStatus(REJEITADO);
            pagamentoGateway.registrarPagamento(pagamento, cartao);
            throw new LimiteExcedidoCartaoException("Limite disponível insuficiente para realizar o pagamento!");
        }
    }

    public List<Pagamento> buscarPagamentosPorChaveCliente(String cpfCliente) {
        Cliente cliente = clienteUseCase.buscaClientePorCpf(cpfCliente);

        List<Cartao> cartoes = cartaoUseCase.buscarCartoesPorCpf(cliente.getCpf());

        if (isNull(cartoes) || cartoes.isEmpty()) {
            throw new CartaoNaoExisteException("Nenhum cartão encontrado para o cliente");
        }

        List<UUID> cartaoIds = cartoes.stream()
                .map(Cartao::getId)
                .toList();

        List<Pagamento> pagamentos = pagamentoGateway.buscarPagamentosPorCartoes(cartaoIds);

        if (isNull(pagamentos) || pagamentos.isEmpty()) {
            throw new PagamentoNaoEncontradoException("Nenhum pagamento encontrado para o cliente");
        }

        return pagamentos;

    }
}
