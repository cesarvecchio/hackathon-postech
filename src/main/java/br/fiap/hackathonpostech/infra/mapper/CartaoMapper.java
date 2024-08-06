package br.fiap.hackathonpostech.infra.mapper;

import br.fiap.hackathonpostech.application.controller.request.CartaoRequest;
import br.fiap.hackathonpostech.application.controller.response.CartaoResponse;
import br.fiap.hackathonpostech.domain.entity.Cartao;
import br.fiap.hackathonpostech.infra.persistence.entity.CartaoEntity;

public class CartaoMapper {
    private CartaoMapper() {
    }

    public static CartaoEntity cartaoToEntity(Cartao cartao) {
        return new CartaoEntity(
                cartao.getId(),
                cartao.getCpf(),
                cartao.getLimite(),
                cartao.getNumero(),
                cartao.getDataValidade(),
                cartao.getCvv()
        );
    }

    public static Cartao entityToCartao(CartaoEntity cartaoEntity) {
        return Cartao.builder()
                .id(cartaoEntity.getId())
                .cpf(cartaoEntity.getCpf())
                .limite(cartaoEntity.getLimite())
                .numero(cartaoEntity.getNumero())
                .dataValidade(cartaoEntity.getDataValidade())
                .cvv(cartaoEntity.getCvv())
                .build();
    }

    public static Cartao requestToCartao(CartaoRequest cartaoRequest) {
        return Cartao.builder()
                .cpf(cartaoRequest.cpf())
                .limite(cartaoRequest.limite())
                .numero(cartaoRequest.numero())
                .dataValidade(cartaoRequest.dataValidade())
                .cvv(cartaoRequest.cvv())
                .build();
    }

    public static CartaoResponse cartaoToResponse(Cartao cartao){
        return new CartaoResponse(
                cartao.getNumero(),
                cartao.getDataValidade(),
                cartao.getCvv(),
                cartao.getLimite()
        );
    }
}
