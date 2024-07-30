package br.fiap.hackathonpostech.application.gateway;

import br.fiap.hackathonpostech.domain.entity.Cartao;
import br.fiap.hackathonpostech.domain.entity.Pagamento;

public interface PagamentoGateway {

    Pagamento registrarPagamento(Pagamento pagamento, Cartao cartao);
}
