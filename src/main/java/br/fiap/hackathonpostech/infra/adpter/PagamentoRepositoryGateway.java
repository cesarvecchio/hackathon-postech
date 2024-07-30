package br.fiap.hackathonpostech.infra.adpter;

import br.fiap.hackathonpostech.application.gateway.PagamentoGateway;
import br.fiap.hackathonpostech.domain.entity.Cartao;
import br.fiap.hackathonpostech.domain.entity.Pagamento;
import br.fiap.hackathonpostech.infra.persistence.repository.PagamentoRepository;

public class PagamentoRepositoryGateway implements PagamentoGateway {

    private PagamentoRepository pagamentoRepository;

    public PagamentoRepositoryGateway(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    @Override
    public Pagamento registrarPagamento(Pagamento pagamento, Cartao cartao) {
        return null; //TODO: IMPLEMENTAR RETORNO REAL
    }
}
