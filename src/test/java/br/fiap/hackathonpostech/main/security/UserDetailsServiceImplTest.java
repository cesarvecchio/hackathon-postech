package br.fiap.hackathonpostech.main.security;

import br.fiap.hackathonpostech.application.exceptions.UsuarioNaoExisteException;
import br.fiap.hackathonpostech.application.gateway.UsuarioGateway;
import br.fiap.hackathonpostech.domain.entity.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDetailsServiceImplTest {
    @Mock
    private UsuarioGateway usuarioGateway;

    private UserDetailsServiceImpl userDetailsService;

    AutoCloseable mock;
    @BeforeEach
    public void setup() {
        mock = MockitoAnnotations.openMocks(this);

        userDetailsService = new UserDetailsServiceImpl(usuarioGateway);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void deveRetornarUsuarioDetails() {
        var usuario = buildUsuario();
        var userDetail = new UserDetailsImpl(usuario);

        when(usuarioGateway.encontrarPorUsuario(usuario.getUsuario())).thenReturn(usuario);

        var resultado = userDetailsService.loadUserByUsername(usuario.getUsuario());

        assertEquals(userDetail.getUsername(), resultado.getUsername());
        assertEquals(userDetail.getPassword(), resultado.getPassword());
        assertEquals(userDetail.isAccountNonExpired(), resultado.isAccountNonExpired());
        assertEquals(userDetail.isAccountNonLocked(), resultado.isAccountNonLocked());
        assertEquals(userDetail.isCredentialsNonExpired(), resultado.isCredentialsNonExpired());
        assertEquals(userDetail.isEnabled(), resultado.isEnabled());

        verify(usuarioGateway).encontrarPorUsuario(usuario.getUsuario());
    }

    @Test
    void deveGerarUsuarioNaoExisteException() {
        var usuario = buildUsuario();
        var userDetail = new UserDetailsImpl(usuario);

        when(usuarioGateway.encontrarPorUsuario(usuario.getUsuario())).thenReturn(null);

        var exception = assertThrows(UsuarioNaoExisteException.class, () -> {
            userDetailsService.loadUserByUsername(usuario.getUsuario());
        });

        assertEquals("Usuario especificado não existe!", exception.getMessage());

        verify(usuarioGateway).encontrarPorUsuario(usuario.getUsuario());
    }

    private Usuario buildUsuario(){
        return new Usuario("adj2", "adj@1234");
    }

}
