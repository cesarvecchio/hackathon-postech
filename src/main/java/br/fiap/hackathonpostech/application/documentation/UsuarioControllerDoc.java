package br.fiap.hackathonpostech.application.documentation;

import br.fiap.hackathonpostech.application.controller.request.UsuarioRequest;
import br.fiap.hackathonpostech.application.controller.response.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Usuario", description = "Usuario API")
public interface UsuarioControllerDoc {

    @Operation(summary = "Logar usuario", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario logado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Erro de autorização"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    ResponseEntity<UsuarioResponse> login(@RequestBody UsuarioRequest usuarioRequest);
}
