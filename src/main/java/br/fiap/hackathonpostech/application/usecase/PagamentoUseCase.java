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
        //cartao.setLimite(cartao.getLimite() - pagamento.getValor());

        return pagamentoGateway.registrarPagamento(pagamento, cartao);
    }

    private void validarLimiteDisponivel(Cartao cartao, Pagamento pagamento) {

        if(cartao.getLimite() <= pagamento.getValor()){
            pagamento.setStatus(REJEITADO);
            pagamentoGateway.registrarPagamento(pagamento, cartao);
            throw new LimiteExcedidoCartaoException("Limite disponível insuficiente para realizar o pagamento!");
        }
    }

    public List<Pagamento> buscarPagamentosPorChaveCliente(UUID idCliente) {
        Cliente cliente = clienteUseCase.buscaClientePorId(idCliente);
        if (null == cliente) {
            throw new ClienteNaoExisteException("Cliente inexistente");
        }

        List<Cartao> cartoes = cartaoUseCase.buscarCartoesPorCpf(cliente.getCpf());

        if (cartoes == null || cartoes.isEmpty()) {
            throw new CartaoNaoExisteException("Nenhum cartão encontrado para o cliente");
        }

        List<UUID> cartaoIds = cartoes.stream()
                .map(Cartao::getId)
                .toList();

        List<Pagamento> pagamentos = pagamentoGateway.buscarPagamentosPorCartoes(cartaoIds);

        if (pagamentos == null || pagamentos.isEmpty()) {
            throw new PagamentoNaoEncontradoException("Nenhum pagamento encontrado para o cliente");
        }

        return pagamentos;

    }
}
