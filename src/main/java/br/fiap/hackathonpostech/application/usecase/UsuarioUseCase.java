package br.fiap.hackathonpostech.application.usecase;

import br.fiap.hackathonpostech.application.exceptions.UsuarioExisteException;
import br.fiap.hackathonpostech.application.exceptions.UsuarioNaoExisteException;
import br.fiap.hackathonpostech.application.gateway.UsuarioGateway;
import br.fiap.hackathonpostech.domain.entity.Usuario;
import br.fiap.hackathonpostech.domain.vo.LoginVO;
import br.fiap.hackathonpostech.infra.mapper.LoginMapper;
import br.fiap.hackathonpostech.infra.mapper.UsuarioMapper;
import br.fiap.hackathonpostech.main.security.TokenService;
import br.fiap.hackathonpostech.main.security.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UsuarioUseCase {
    private final UsuarioGateway usuarioGateway;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UsuarioUseCase(UsuarioGateway usuarioGateway, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.usuarioGateway = usuarioGateway;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public LoginVO login(Usuario usuario) {
        Authentication usernamePassword = new UsernamePasswordAuthenticationToken(
                usuario.getUsuario(), usuario.getSenha());

        try {
            Authentication auth = authenticationManager.authenticate(usernamePassword);

            return LoginMapper.stringToLoginResponse(tokenService.generateToken((UserDetailsImpl) auth.getPrincipal()));

        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Usuario ou senha errados");
        }

//        return usuarioGateway.login(usuario.getUsuario(), usuario.getSenha());
    }

    public Usuario registrar(Usuario usuario) {
        if (usuarioGateway.encontrarPorUsuario(usuario.getUsuario()) != null) {
            throw new UsuarioExisteException("Usuario informado já existe");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(usuario.getSenha());
        usuario.setSenha(encryptedPassword);

        return usuarioGateway.registrarUsuario(usuario);
    }

    public Usuario encontrarPorUsuario(String usuario) {
        Usuario usuarioDomain = usuarioGateway.encontrarPorUsuario(usuario);

        if(usuarioDomain == null) {
            throw new UsuarioNaoExisteException("Usuario especificado não existe!");
        }

        return usuarioDomain;
    }

//    public LoginVO autenticarUsuario(User user) {
//        Authentication usernamePassword = new UsernamePasswordAuthenticationToken(user.getUsuario(), user.getSenha());
//
//        try {
//            Authentication auth = authenticationManager.authenticate(usernamePassword);
//            return LoginMapper.stringToLoginResponse(tokenService.generateToken((UserDetailsImpl) auth.getPrincipal()));
//        } catch (AuthenticationException ex) {
//            throw new BadCredentialsException("Wrong username or password");
//        }
//    }
}
