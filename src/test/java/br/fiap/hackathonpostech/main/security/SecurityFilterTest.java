package br.fiap.hackathonpostech.main.security;

import br.fiap.hackathonpostech.application.gateway.UsuarioGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SecurityFilterTest {
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


}
