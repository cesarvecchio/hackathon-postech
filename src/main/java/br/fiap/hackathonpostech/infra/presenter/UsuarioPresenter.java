package br.fiap.hackathonpostech.infra.presenter;

import br.fiap.hackathonpostech.application.controller.response.UsuarioResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class UsuarioPresenter {
    private UsuarioPresenter() {
    }

    public static ResponseEntity<UsuarioResponse> toResponseEntity(HttpStatusCode statusCode, UsuarioResponse usuarioResponse) {
        return ResponseEntity.status(statusCode).body(usuarioResponse);
    }
}
