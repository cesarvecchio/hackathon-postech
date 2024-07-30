package br.fiap.hackathonpostech.infra.adpter;

import br.fiap.hackathonpostech.application.gateway.UsuarioGateway;
import br.fiap.hackathonpostech.domain.entity.Usuario;
import br.fiap.hackathonpostech.infra.mapper.UsuarioMapper;
import br.fiap.hackathonpostech.infra.persistence.repository.UsuarioRepository;

public class UsuarioRepositoryGateway implements UsuarioGateway {
    private final UsuarioRepository usuarioRepository;

    public UsuarioRepositoryGateway(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario login(String usuario, String senha) {
        return usuarioRepository.findByUsuarioAndSenha(usuario, senha)
                .map(UsuarioMapper::entityToUsuario)
                .orElse(null);

    }

    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        return UsuarioMapper.entityToUsuario(
                usuarioRepository.save(
                        UsuarioMapper.userToEntity(usuario)
                ));
    }

    @Override
    public Usuario autenticarUsuario(Usuario usuario) {
        return null;
    }

    @Override
    public Usuario encontrarPorUsuario(String usuario) {
        return usuarioRepository.findByUsuario(usuario)
                .map(UsuarioMapper::entityToUsuario)
                .orElse(null);

    }
}
