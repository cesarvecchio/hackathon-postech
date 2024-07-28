package br.fiap.hackathonpostech.infra.presenter;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class CartaoPresenter {
    private CartaoPresenter() {
    }

    public static ResponseEntity<Void> toResponseEntitu(HttpStatusCode statusCode) {
        return ResponseEntity.status(statusCode).build();
    }
}
