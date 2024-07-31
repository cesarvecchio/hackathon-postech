package br.fiap.hackathonpostech.infra.adpter;

import br.fiap.hackathonpostech.application.gateway.PagamentoGateway;
import br.fiap.hackathonpostech.domain.entity.Cartao;
import br.fiap.hackathonpostech.domain.entity.Pagamento;
import br.fiap.hackathonpostech.infra.mapper.CartaoMapper;
import br.fiap.hackathonpostech.infra.mapper.ClienteMapper;
import br.fiap.hackathonpostech.infra.mapper.PagamentoMapper;
import br.fiap.hackathonpostech.infra.persistence.entity.PagamentoEntity;
import br.fiap.hackathonpostech.infra.persistence.repository.PagamentoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Override
    public List<Pagamento> buscarPagamentosPorCartoes(List<UUID> idCartoes) {
        return pagamentoRepository.findByCartaoIdIn(idCartoes).stream()
                .map(PagamentoMapper::entityToPagamento)
                .collect(Collectors.toList());
    }
}
