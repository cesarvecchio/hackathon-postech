package br.fiap.hackathonpostech.application.controller;

import br.fiap.hackathonpostech.application.controller.request.UsuarioRequest;
import br.fiap.hackathonpostech.application.controller.response.UsuarioResponse;
import br.fiap.hackathonpostech.application.usecase.UsuarioUseCase;
import br.fiap.hackathonpostech.domain.entity.Usuario;
import br.fiap.hackathonpostech.domain.vo.LoginVO;
import br.fiap.hackathonpostech.infra.mapper.UsuarioMapper;
import br.fiap.hackathonpostech.infra.presenter.UsuarioPresenter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autenticacao")
public class UsuarioController {
    private final UsuarioUseCase usuarioUseCase;

    public UsuarioController(UsuarioUseCase usuarioUseCase) {
        this.usuarioUseCase = usuarioUseCase;
    }

    @PostMapping
    public ResponseEntity<LoginVO> login(@RequestBody UsuarioRequest usuarioRequest) {
        LoginVO retorno = usuarioUseCase.login(UsuarioMapper.requestToUsuario(usuarioRequest));

        return ResponseEntity.ok(retorno);
    }

}
