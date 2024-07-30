package br.fiap.hackathonpostech.application.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record ClienteResponse(@JsonProperty("id_cliente") UUID idCliente) {
}
