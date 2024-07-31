package br.fiap.hackathonpostech.application.controller;

import br.fiap.hackathonpostech.application.controller.request.UsuarioRequest;
import br.fiap.hackathonpostech.application.controller.response.UsuarioResponse;
import br.fiap.hackathonpostech.application.exceptions.ControllerExceptionHandler;
import br.fiap.hackathonpostech.application.exceptions.StandardErrorException;
import br.fiap.hackathonpostech.application.exceptions.UsuarioNaoExisteException;
import br.fiap.hackathonpostech.application.usecase.UsuarioUseCase;
import br.fiap.hackathonpostech.infra.mapper.UsuarioMapper;
import br.fiap.hackathonpostech.main.security.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UsuarioControllerTest {
    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();
    MockMvc mockMvc;
    AutoCloseable mock;

    private final TokenService tokenService = new TokenService();
    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void setup() {
        mock = MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(tokenService, "secret", "secret");
        ReflectionTestUtils.setField(tokenService, "issuer", "issuer");

        UsuarioUseCase usuarioUseCase = new UsuarioUseCase(authenticationManager, tokenService);
        UsuarioController usuarioController = new UsuarioController(usuarioUseCase);

        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void login() throws Exception {
        var request = usuarioRequest();
        var usuario = UsuarioMapper.requestToUsuario(request);
        var usernamePassword = new UsernamePasswordAuthenticationToken(
                usuario.getUsuario(), usuario.getSenha());
        var auth = new UsernamePasswordAuthenticationToken(usuario.getUsuario(), usuario.getSenha());

        when(authenticationManager.authenticate(usernamePassword)).thenReturn(auth);

        mockMvc.perform(post("/autenticacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    UsuarioResponse response = jsonToObject(result.getResponse().getContentAsString(),
                            UsuarioResponse.class);
                    var decodeToken = tokenService.validateToken(response.token());

                    assertEquals(request.usuario(), decodeToken);
                });
    }

    @Test
    void loginGeraUsuarioNaoExisteException() throws Exception {
        var request = usuarioRequest();
        var usuario = UsuarioMapper.requestToUsuario(request);
        var usernamePassword = new UsernamePasswordAuthenticationToken(
                usuario.getUsuario(), usuario.getSenha());

        doThrow(new BadCredentialsException(
                "Usuario especificado não existe!",
                new UsuarioNaoExisteException("Usuario especificado não existe!"))
        ).when(authenticationManager).authenticate(usernamePassword);

        mockMvc.perform(post("/autenticacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isForbidden())
                .andExpect(result -> {
                    StandardErrorException response = jsonToObject(result.getResponse().getContentAsString(),
                            StandardErrorException.class);

                    assertEquals("Usuario especificado não existe!", response.getMessage());
                });
    }

    @Test
    void loginGeraBadCredentialsException() throws Exception {
        var request = usuarioRequest();
        var usuario = UsuarioMapper.requestToUsuario(request);
        var usernamePassword = new UsernamePasswordAuthenticationToken(
                usuario.getUsuario(), usuario.getSenha());

        doThrow(new BadCredentialsException(
                "Senha errada!", null)
        ).when(authenticationManager).authenticate(usernamePassword);

        mockMvc.perform(post("/autenticacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> {
                    StandardErrorException response = jsonToObject(result.getResponse().getContentAsString(),
                            StandardErrorException.class);

                    assertEquals("Senha errada!", response.getMessage());
                });
    }

    private UsuarioRequest usuarioRequest() {
        return new UsuarioRequest("adj2", "adj@1234");
    }

    private static String asJsonString(Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    private <T> T jsonToObject(String json, Class<T> classz) throws JsonProcessingException {
        return MAPPER.readValue(json, classz);
    }
}
