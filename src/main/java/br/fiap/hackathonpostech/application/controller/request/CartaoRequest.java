package br.fiap.hackathonpostech.application.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CartaoRequest(
        String cpf,
        Double limite,
        String numero,
        @JsonProperty("data_validade")
        String dataValidade,
        String cvv
) {
}
