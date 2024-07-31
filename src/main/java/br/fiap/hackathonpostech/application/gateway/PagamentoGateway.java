package br.fiap.hackathonpostech.application.gateway;

import br.fiap.hackathonpostech.domain.entity.Cartao;
import br.fiap.hackathonpostech.domain.entity.Pagamento;

import java.util.List;
import java.util.UUID;

public interface PagamentoGateway {

    Pagamento registrarPagamento(Pagamento pagamento, Cartao cartao);

    List<Pagamento> buscarPagamentosPorCartoes(List<UUID> idCartoes);
}
