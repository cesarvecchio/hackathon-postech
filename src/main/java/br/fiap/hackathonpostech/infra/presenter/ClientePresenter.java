package br.fiap.hackathonpostech.infra.presenter;

import br.fiap.hackathonpostech.application.controller.response.ClienteResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ClientePresenter {

    private ClientePresenter() {
    }

    public static ResponseEntity<ClienteResponse> toResponseEntity(ClienteResponse clienteResponse, HttpStatusCode statusCode) {
        return ResponseEntity.status(statusCode).body(clienteResponse);
    }
}
