package br.fiap.hackathonpostech.application.controller;

import br.fiap.hackathonpostech.application.controller.request.PagamentoRequest;
import br.fiap.hackathonpostech.application.controller.response.PagamentoListResponse;
import br.fiap.hackathonpostech.application.controller.response.PagamentoResponse;
import br.fiap.hackathonpostech.application.documentation.PagamentoControllerDoc;
import br.fiap.hackathonpostech.application.usecase.PagamentoUseCase;
import br.fiap.hackathonpostech.domain.entity.Pagamento;
import br.fiap.hackathonpostech.infra.mapper.PagamentoMapper;
import br.fiap.hackathonpostech.infra.presenter.PagamentoPresenter;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController implements PagamentoControllerDoc {

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

    @GetMapping(value = "/{chave}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PagamentoListResponse>> buscarPagamentosPorChaveCliente(@Valid @PathVariable("chave") String chave) {
        List<Pagamento> pagamentos = pagamentoUseCase.buscarPagamentosPorChaveCliente(chave);

        List<PagamentoListResponse> pagamentosResponse = pagamentos.stream()
                .map(pagamento -> new PagamentoListResponse(
                        pagamento.getValor(),
                        pagamento.getDescricao(),
                        pagamento.getMetodoPagamento().getValor(),
                        pagamento.getStatus().getValor()
                ))
                .toList();

        return PagamentoPresenter.toResponseEntityList(pagamentosResponse, HttpStatusCode.valueOf(200));
    }
}
