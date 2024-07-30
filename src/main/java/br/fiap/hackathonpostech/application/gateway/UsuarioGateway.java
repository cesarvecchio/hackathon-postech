package br.fiap.hackathonpostech.application.gateway;

import br.fiap.hackathonpostech.domain.entity.Usuario;

public interface UsuarioGateway {

    Usuario login(String usuario, String senha);

    Usuario registrarUsuario(Usuario usuario);

    Usuario autenticarUsuario(Usuario usuario);

    Usuario encontrarPorUsuario(String usuario);

}
