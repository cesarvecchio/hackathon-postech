package br.fiap.hackathonpostech.infra.mapper;

import br.fiap.hackathonpostech.application.controller.request.UsuarioRequest;
import br.fiap.hackathonpostech.application.controller.response.UsuarioResponse;
import br.fiap.hackathonpostech.domain.entity.Usuario;
import br.fiap.hackathonpostech.domain.vo.LoginVO;
import br.fiap.hackathonpostech.infra.persistence.entity.UsuarioEntity;


public class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static Usuario entityToUsuario(UsuarioEntity usuarioEntity) {
        return new Usuario(usuarioEntity.getUsuario(), usuarioEntity.getSenha());
    }

    public static Usuario requestToUsuario(UsuarioRequest usuarioRequest) {
        return new Usuario(usuarioRequest.usuario(), usuarioRequest.senha());
    }

    public static UsuarioResponse loginToResponse(LoginVO loginVO) {
        return new UsuarioResponse(loginVO.token());
    }
}
