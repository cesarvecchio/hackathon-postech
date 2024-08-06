package br.fiap.hackathonpostech.domain.entity;

import br.fiap.hackathonpostech.application.exceptions.CodigoCartaoInvalidoException;
import br.fiap.hackathonpostech.application.exceptions.ValidadeCartaoException;
import br.fiap.hackathonpostech.domain.enums.MetodoPagamentoEnum;
import br.fiap.hackathonpostech.domain.enums.StatusEnum;
import lombok.Builder;
import lombok.Data;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Data
@Builder
public class Pagamento {
    private UUID id;
    private Double valor;
    private String descricao;
    private MetodoPagamentoEnum metodoPagamento;
    private StatusEnum status;
    private String cpf;
    private String numero;
    private String dataValidade;
    private String cvv;

    public void validarCodigoCartao(Cartao cartao) {
        if (!cartao.getCvv().equals(cvv)) {
            throw new CodigoCartaoInvalidoException("Código informado inválido!");
        }
    }

    public void validarDataExpiracao(Cartao cartao) {

        if (!cartao.getDataValidade().equals(dataValidade)) {
            throw new ValidadeCartaoException("Data de validade do cartão incorreta!");
        }

        if (isDataExpirada(cartao.getDataValidade())) {
            throw new ValidadeCartaoException("Data de validade do cartão expirada!");
        }
    }

    private boolean isDataExpirada(String dataValidade) {
        DateTimeFormatter dataFormatada = DateTimeFormatter.ofPattern("MM/yy");
        try {
            YearMonth validade = YearMonth.parse(dataValidade, dataFormatada);

            // Obter o ano e mês atual
            YearMonth anoMesAtual = YearMonth.now();

            // Comparar a data de validade com a data atual
            return validade.isBefore(anoMesAtual);
        } catch (DateTimeParseException e) {
            throw new ValidadeCartaoException("Formato de data inválido. Deve ser MM/yy.");
        }
    }
}
