package br.fiap.hackathonpostech.application.controller;

import br.fiap.hackathonpostech.application.controller.request.PagamentoRequest;
import br.fiap.hackathonpostech.application.controller.response.PagamentoResponse;
import br.fiap.hackathonpostech.application.usecase.PagamentoUseCase;
import br.fiap.hackathonpostech.domain.entity.Pagamento;
import br.fiap.hackathonpostech.infra.mapper.PagamentoMapper;
import br.fiap.hackathonpostech.infra.presenter.PagamentoPresenter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoUseCase pagamentoUseCase;

    public PagamentoController(PagamentoUseCase pagamentoUseCase) {
        this.pagamentoUseCase = pagamentoUseCase;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagamentoResponse> registrarPagamento(@Valid @RequestBody PagamentoRequest pagamentoRequest) {
        Pagamento pagamento = pagamentoUseCase.registrarPagamento(PagamentoMapper.requestToPagamento(pagamentoRequest));

        PagamentoResponse pagamentoResponse = new PagamentoResponse(pagamento.getId());

        return PagamentoPresenter.toResponseEntity(pagamentoResponse, HttpStatusCode.valueOf(200));
    }
}
