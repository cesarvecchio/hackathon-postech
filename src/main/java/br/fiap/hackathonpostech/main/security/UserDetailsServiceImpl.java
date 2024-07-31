package br.fiap.hackathonpostech.main.security;

import br.fiap.hackathonpostech.application.exceptions.UsuarioNaoExisteException;
import br.fiap.hackathonpostech.application.gateway.UsuarioGateway;
import br.fiap.hackathonpostech.domain.entity.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsuarioGateway usuarioGateway;

    public UserDetailsServiceImpl(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario =  usuarioGateway.encontrarPorUsuario(username);

        if(usuario == null) {
            throw new UsuarioNaoExisteException("Usuario especificado não existe!");
        }

        return new UserDetailsImpl(usuario);
    }
}
