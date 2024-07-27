package br.fiap.hackathonpostech.application.controller;

import br.fiap.hackathonpostech.application.usecase.UserUseCase;
import br.fiap.hackathonpostech.domain.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autenticacao")
public class UserController {
    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @GetMapping
    public User login() {
        User retorno = userUseCase.login("", "");

        return retorno;
    }
}
