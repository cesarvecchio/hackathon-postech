package br.fiap.hackathonpostech.application.usecase;

import br.fiap.hackathonpostech.domain.entity.Usuario;
import br.fiap.hackathonpostech.domain.vo.LoginVO;
import br.fiap.hackathonpostech.infra.mapper.LoginMapper;
import br.fiap.hackathonpostech.main.security.TokenService;
import br.fiap.hackathonpostech.main.security.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class UsuarioUseCase {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UsuarioUseCase(AuthenticationManager authenticationManager, TokenService tokenService) {
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
    }
}
