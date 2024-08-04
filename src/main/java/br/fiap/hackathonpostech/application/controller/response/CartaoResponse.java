package br.fiap.hackathonpostech.application.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CartaoResponse(
        String numero,
        @JsonProperty("data_validade")
        String dataValidade,
        String cvv,
        Double limite
) {
}
