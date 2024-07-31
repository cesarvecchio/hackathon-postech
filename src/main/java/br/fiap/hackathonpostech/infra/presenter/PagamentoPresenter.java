package br.fiap.hackathonpostech.infra.presenter;

import br.fiap.hackathonpostech.application.controller.response.PagamentoListResponse;
import br.fiap.hackathonpostech.application.controller.response.PagamentoResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class PagamentoPresenter {

    private PagamentoPresenter() {
    }

    public static ResponseEntity<PagamentoResponse> toResponseEntity(PagamentoResponse pagamentoResponse, HttpStatusCode statusCode) {
        return ResponseEntity.status(statusCode).body(pagamentoResponse);
    }

    public static ResponseEntity<List<PagamentoListResponse>> toResponseEntityList(List<PagamentoListResponse> pagamentoResponseList, HttpStatusCode statusCode) {
        return ResponseEntity.status(statusCode).body(pagamentoResponseList);
    }
}
