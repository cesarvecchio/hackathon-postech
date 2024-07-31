package br.fiap.hackathonpostech.main.security;

import br.fiap.hackathonpostech.application.gateway.UsuarioGateway;
import br.fiap.hackathonpostech.domain.entity.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UsuarioGateway usuarioGateway;

    public SecurityFilter(TokenService tokenService, UsuarioGateway usuarioGateway) {
        this.tokenService = tokenService;
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> token = recoverToken(request);

        if (token.isPresent()) {
            String login = tokenService.validateToken(token.get());

            Usuario usuario = usuarioGateway.encontrarPorUsuario(login);

            UserDetails userDetails = new UserDetailsImpl(usuario);

            Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> recoverToken(HttpServletRequest request) {
        Optional<String> authHeader = ofNullable(request.getHeader("Authorization"));
        if (authHeader.isPresent()) {
            return authHeader.map(a -> a.replace("Bearer ", ""));
        }
        return empty();
    }
}
