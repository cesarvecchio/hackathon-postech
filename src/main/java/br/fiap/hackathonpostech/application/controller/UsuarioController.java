package br.fiap.hackathonpostech.application.controller;

import br.fiap.hackathonpostech.application.controller.request.UsuarioRequest;
import br.fiap.hackathonpostech.application.controller.response.UsuarioResponse;
import br.fiap.hackathonpostech.application.documentation.UsuarioControllerDoc;
import br.fiap.hackathonpostech.application.usecase.UsuarioUseCase;
import br.fiap.hackathonpostech.domain.vo.LoginVO;
import br.fiap.hackathonpostech.infra.mapper.UsuarioMapper;
import br.fiap.hackathonpostech.infra.presenter.UsuarioPresenter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autenticacao")
public class UsuarioController implements UsuarioControllerDoc {
    private final UsuarioUseCase usuarioUseCase;

    public UsuarioController(UsuarioUseCase usuarioUseCase) {
        this.usuarioUseCase = usuarioUseCase;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> login(@RequestBody UsuarioRequest usuarioRequest) {
        LoginVO retorno = usuarioUseCase.login(UsuarioMapper.requestToUsuario(usuarioRequest));

        return UsuarioPresenter.toResponseEntity(
                HttpStatusCode.valueOf(200),
                UsuarioMapper.loginToResponse(retorno)
        );
    }

}
