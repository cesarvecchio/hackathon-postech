package br.fiap.hackathonpostech.application.usecase;

import br.fiap.hackathonpostech.application.exceptions.CartaoNaoExisteException;
import br.fiap.hackathonpostech.application.exceptions.CodigoCartaoInvalidoException;
import br.fiap.hackathonpostech.application.exceptions.ValidadeCartaoException;
import br.fiap.hackathonpostech.application.gateway.PagamentoGateway;
import br.fiap.hackathonpostech.domain.entity.Cartao;
import br.fiap.hackathonpostech.domain.entity.Pagamento;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
        validarDataExpiracao(cartao, pagamento.getDataValidade());

        return null; // TODO: IMPLEMENTAR RETORNO REAL
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
}
