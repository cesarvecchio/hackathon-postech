package br.fiap.hackathonpostech.application.controller;

import br.fiap.hackathonpostech.application.controller.request.CartaoRequest;
import br.fiap.hackathonpostech.application.documentation.CartaoControllerDoc;
import br.fiap.hackathonpostech.application.usecase.CartaoUseCase;
import br.fiap.hackathonpostech.infra.mapper.CartaoMapper;
import br.fiap.hackathonpostech.infra.presenter.CartaoPresenter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cartao")
public class CartaoController implements CartaoControllerDoc {

    private final CartaoUseCase cartaoUseCase;

    public CartaoController(CartaoUseCase cartaoUseCase) {
        this.cartaoUseCase = cartaoUseCase;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gerarCartao(@Valid @RequestBody CartaoRequest cartaoRequest) {
        cartaoUseCase.gerarCartao(CartaoMapper.requestToCartao(cartaoRequest));

        return CartaoPresenter.toResponseEntitu(HttpStatusCode.valueOf(200));
    }
}
