package br.fiap.hackathonpostech.application.controller;

import br.fiap.hackathonpostech.application.controller.request.ClienteRequest;
import br.fiap.hackathonpostech.application.controller.response.ClienteResponse;
import br.fiap.hackathonpostech.application.documentation.ClienteControllerDoc;
import br.fiap.hackathonpostech.application.usecase.ClienteUseCase;
import br.fiap.hackathonpostech.domain.entity.Cliente;
import br.fiap.hackathonpostech.infra.mapper.ClienteMapper;
import br.fiap.hackathonpostech.infra.presenter.ClientePresenter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
public class ClienteController implements ClienteControllerDoc {

    private final ClienteUseCase clienteUseCase;

    public ClienteController(ClienteUseCase clienteUseCase) {
        this.clienteUseCase = clienteUseCase;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClienteResponse> registrarCliente(@Valid @RequestBody ClienteRequest clienteRequest) {
        Cliente cliente = clienteUseCase.registrarCliente(ClienteMapper.requestToCliente(clienteRequest));

        ClienteResponse clienteResponse = new ClienteResponse(cliente.getId().toString());

        return ClientePresenter.toResponseEntity(clienteResponse, HttpStatusCode.valueOf(200));
    }
}
