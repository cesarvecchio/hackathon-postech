package br.fiap.hackathonpostech.application.documentation;

import br.fiap.hackathonpostech.application.controller.request.PagamentoRequest;
import br.fiap.hackathonpostech.application.controller.response.PagamentoListResponse;
import br.fiap.hackathonpostech.application.controller.response.PagamentoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@Tag(name = "Pagamento", description = "Pagamento API")
public interface PagamentoControllerDoc {

    @Operation(summary = "Registrar um pagamento", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagamento realizado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Erro de autenticação"),
        @ApiResponse(responseCode = "402", description = "Limite do cartão estourado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    ResponseEntity<PagamentoResponse> registrarPagamento(@RequestBody(description = "Informações para registrar um pagamento") PagamentoRequest pagamentoRequest);

    @Operation(summary = "Consultar pagamentos de um cliente", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagamentos retornados com sucesso"),
        @ApiResponse(responseCode = "401", description = "Erro de autenticação"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    ResponseEntity<List<PagamentoListResponse>> buscarPagamentosPorChaveCliente(@Parameter(description = "Chave do cliente") UUID chave);
}
