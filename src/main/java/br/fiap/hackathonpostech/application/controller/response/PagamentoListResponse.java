package br.fiap.hackathonpostech.application.controller.response;

import br.fiap.hackathonpostech.domain.enums.MetodoPagamentoEnum;
import br.fiap.hackathonpostech.domain.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record PagamentoListResponse(
        Double valor,
        String descricao,
        MetodoPagamentoEnum metodoPagamento,
        StatusEnum status
) {
}
