package br.fiap.hackathonpostech.application.usecase;

import br.fiap.hackathonpostech.application.exceptions.*;
import br.fiap.hackathonpostech.application.gateway.PagamentoGateway;
import br.fiap.hackathonpostech.domain.entity.Cartao;
import br.fiap.hackathonpostech.domain.entity.Cliente;
import br.fiap.hackathonpostech.domain.entity.Pagamento;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        validarCodigoCartao(cartao, pagamento.getCvv());
        validarDataExpiracao(cartao, pagamento.getDataValidade());
        validarLimiteDisponivel(cartao, pagamento);

        pagamento.setStatus(APROVADO);
        //cartao.setLimite(cartao.getLimite() - pagamento.getValor());

        return pagamentoGateway.registrarPagamento(pagamento, cartao);
    }

    private void validarCodigoCartao(Cartao cartao, String cvv) {
        if(!cartao.getCvv().equals(cvv)){
            throw new CodigoCartaoInvalidoException("Código informado inválido!");
        }
    }

    private void validarDataExpiracao(Cartao cartao, String dataValidade) {

        if(!cartao.getDataValidade().equals(dataValidade)){
            throw new ValidadeCartaoException("Data de validade do cartão incorreta!");
        }

        if(isDataExpirada(cartao.getDataValidade())){
            throw new ValidadeCartaoException("Data de validade do cartão expirada!");
        }

    }

    public static boolean isDataExpirada(String dataValidade) {
        DateTimeFormatter dataFormatada = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth validade;

        try {
            validade = YearMonth.parse(dataValidade, dataFormatada);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de data inválido. Deve ser MM/yy.", e);
        }

        // Obter o ano e mês atual
        YearMonth anoMesAtual = YearMonth.now();

        // Comparar a data de validade com a data atual
        return validade.isBefore(anoMesAtual);
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

        if (cartoes.isEmpty()) {
            throw new CartaoNaoExisteException("Nenhum cartão encontrado para o cliente");
        }

        List<UUID> cartaoIds = cartoes.stream()
                .map(Cartao::getId)
                .collect(Collectors.toList());

        List<Pagamento> pagamentos = pagamentoGateway.buscarPagamentosPorCartoes(cartaoIds);

        if (pagamentos.isEmpty()) {
            throw new PagamentoNaoEncontradoException("Nenhum pagamento encontrado para o cliente");
        }

        return pagamentos;

    }
}
