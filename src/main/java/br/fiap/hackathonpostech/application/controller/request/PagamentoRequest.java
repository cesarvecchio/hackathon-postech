package br.fiap.hackathonpostech.application.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PagamentoRequest(
        String cpf,
        String numero,
        @JsonProperty("data_validade")
        String dataValidade,
        String cvv,
        Double valor
) {
}
