package br.fiap.hackathonpostech.application.controller;

import br.fiap.hackathonpostech.application.controller.request.ClienteRequest;
import br.fiap.hackathonpostech.application.controller.response.ClienteResponse;
import br.fiap.hackathonpostech.application.usecase.ClienteUseCase;
import br.fiap.hackathonpostech.domain.entity.Cliente;
import br.fiap.hackathonpostech.infra.mapper.ClienteMapper;
import br.fiap.hackathonpostech.infra.presenter.ClientePresenter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteUseCase clienteUseCase;

    public ClienteController(ClienteUseCase clienteUseCase) {
        this.clienteUseCase = clienteUseCase;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> registrarCliente(@RequestBody ClienteRequest clienteRequest) {
        Cliente cliente = clienteUseCase.registrarCliente(ClienteMapper.requestToCliente(clienteRequest));

        ClienteResponse clienteResponse = new ClienteResponse(cliente.getId().toString());

        return ClientePresenter.toResponseEntity(clienteResponse, HttpStatusCode.valueOf(200));
    }
}
