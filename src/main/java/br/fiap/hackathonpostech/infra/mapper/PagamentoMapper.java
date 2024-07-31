package br.fiap.hackathonpostech.infra.mapper;

import br.fiap.hackathonpostech.application.controller.request.PagamentoRequest;
import br.fiap.hackathonpostech.domain.entity.Pagamento;
import br.fiap.hackathonpostech.infra.persistence.entity.PagamentoEntity;

public class PagamentoMapper {
    private PagamentoMapper() {
    }

    public static PagamentoEntity pagamentoToEntity(Pagamento pagamento) {
        return new PagamentoEntity(
                pagamento.getId(),
                pagamento.getValor(),
                pagamento.getDescricao(),
                pagamento.getMetodoPagamento(),
                pagamento.getStatus()
        );
    }

    public static Pagamento entityToPagamento(PagamentoEntity pagamentoEntity) {
        return Pagamento.builder()
                .id(pagamentoEntity.getId())
                .valor(pagamentoEntity.getValor())
                .descricao(pagamentoEntity.getDescricao())
                .metodoPagamento(pagamentoEntity.getMetodoPagamento())
                .status(pagamentoEntity.getStatus())
                .build();
    }

    public static Pagamento requestToPagamento(PagamentoRequest pagamentoRequest) {
        return Pagamento.builder()
                .cpf(pagamentoRequest.cpf())
                .numero(pagamentoRequest.numero())
                .dataValidade(pagamentoRequest.dataValidade())
                .cvv(pagamentoRequest.cvv())
                .valor(pagamentoRequest.valor())
                .build();
    }
}
