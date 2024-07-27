package br.fiap.hackathonpostech.application.documentation;

import br.fiap.hackathonpostech.application.controller.request.ClienteRequest;
import br.fiap.hackathonpostech.application.controller.response.ClienteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Cliente", description = "Cliente API")
public interface ClienteControllerDoc {

    @Operation(summary = "Registrar um cliente", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente registrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro na validação dos dados do cliente"),
        @ApiResponse(responseCode = "401", description = "Erro de autenticação"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    ResponseEntity<ClienteResponse> registrarCliente(@RequestBody(description = "Informações para registrar um cliente") ClienteRequest clienteRequest);
}
