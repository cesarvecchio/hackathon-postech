package br.fiap.hackathonpostech.infra.presenter;

import br.fiap.hackathonpostech.application.controller.response.CartaoResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class CartaoPresenter {
    private CartaoPresenter() {
    }

    public static ResponseEntity<Void> toResponseEntity(HttpStatusCode statusCode) {
        return ResponseEntity.status(statusCode).build();
    }

    public static ResponseEntity<List<CartaoResponse>> toResponseEntity(HttpStatusCode statusCode, List<CartaoResponse> response) {
        return ResponseEntity.status(statusCode).body(response);
    }
}
