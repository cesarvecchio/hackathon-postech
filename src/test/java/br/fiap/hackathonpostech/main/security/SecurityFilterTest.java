package br.fiap.hackathonpostech.main.security;

import br.fiap.hackathonpostech.application.gateway.UsuarioGateway;
import br.fiap.hackathonpostech.domain.entity.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;

class SecurityFilterTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    @Mock
    private TokenService tokenService;
    @Mock
    private UsuarioGateway usuarioGateway;

    private SecurityFilter securityFilter;

    AutoCloseable mock;

    @BeforeEach
    public void setup() {
        mock = MockitoAnnotations.openMocks(this);

        securityFilter = new SecurityFilter(tokenService, usuarioGateway);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void deveFiltrar() throws ServletException, IOException {
        var bearerToken = "Bearer token";
        var token = "token";
        var login = "adj2";
        var usuario = buildUsuario();
        var userDetails = new UserDetailsImpl(usuario);

        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(tokenService.validateToken(token)).thenReturn(login);
        when(usuarioGateway.encontrarPorUsuario(login)).thenReturn(usuario);
        doNothing().when(filterChain).doFilter(request, response);

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(request).getHeader("Authorization");
        verify(tokenService).validateToken(token);
        verify(usuarioGateway).encontrarPorUsuario(login);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void deveFiltrarTokenVazio() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(request, times(1)).getHeader("Authorization");
        verify(tokenService, times(0)).validateToken(anyString());
        verify(usuarioGateway, times(0)).encontrarPorUsuario(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    private Usuario buildUsuario() {
        return new Usuario("adj2", "adj@1234");
    }
}
