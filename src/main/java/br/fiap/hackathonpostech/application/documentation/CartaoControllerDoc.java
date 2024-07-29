package br.fiap.hackathonpostech.application.documentation;

import br.fiap.hackathonpostech.application.controller.response.CartaoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Cartão", description = "Cartão API")
public interface CartaoControllerDoc {
    @Operation(summary = "Registrar um cliente", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cartão registrado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação"),
            @ApiResponse(responseCode = "403", description = "Erro limite de cartões excedido"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    ResponseEntity<Void> gerarCartao(@Valid @RequestBody CartaoRequest cartaoRequest);
}
