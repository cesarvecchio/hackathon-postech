package br.fiap.hackathonpostech.infra.adpter;

import br.fiap.hackathonpostech.application.gateway.PagamentoGateway;
import br.fiap.hackathonpostech.domain.entity.Cartao;
import br.fiap.hackathonpostech.domain.entity.Pagamento;
import br.fiap.hackathonpostech.infra.mapper.CartaoMapper;
import br.fiap.hackathonpostech.infra.mapper.PagamentoMapper;
import br.fiap.hackathonpostech.infra.persistence.entity.PagamentoEntity;
import br.fiap.hackathonpostech.infra.persistence.repository.PagamentoRepository;

public class PagamentoRepositoryGateway implements PagamentoGateway {

    private final PagamentoRepository pagamentoRepository;

    public PagamentoRepositoryGateway(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    @Override
    public Pagamento registrarPagamento(Pagamento pagamento, Cartao cartao) {
        PagamentoEntity pagamentoEntity = PagamentoMapper.pagamentoToEntity(pagamento);
        pagamentoEntity.setCartao(CartaoMapper.cartaoToEntity(cartao));

        return PagamentoMapper.entityToPagamento(pagamentoRepository.save(pagamentoEntity));
    }
}
