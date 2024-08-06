package br.fiap.hackathonpostech.application.documentation;

import br.fiap.hackathonpostech.application.controller.request.CartaoRequest;
import br.fiap.hackathonpostech.application.controller.response.CartaoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Cartão", description = "Cartão API")
public interface CartaoControllerDoc extends Authentication{
    @Operation(summary = "Registrar um cliente", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cartão registrado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação"),
            @ApiResponse(responseCode = "403", description = "Erro limite de cartões excedido"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @SecurityRequirement(name = "Authorization")
    ResponseEntity<Void> gerarCartao(@Valid @RequestBody CartaoRequest cartaoRequest);

    @Operation(summary = "Buscar cartões por cpf", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cartão registrado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @SecurityRequirement(name = "Authorization")
    ResponseEntity<List<CartaoResponse>> buscarCartoesPorCpf(@Valid @PathVariable("cpf") String cpf);
}
