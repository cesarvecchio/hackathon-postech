package br.fiap.hackathonpostech.application.gateway;

import br.fiap.hackathonpostech.domain.entity.Usuario;

public interface UsuarioGateway {

    Usuario encontrarPorUsuario(String usuario);

}
