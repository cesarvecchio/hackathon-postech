package br.fiap.hackathonpostech.application.controller;

import br.fiap.hackathonpostech.application.controller.response.CartaoRequest;
import br.fiap.hackathonpostech.application.controller.response.ClienteResponse;
import br.fiap.hackathonpostech.application.exceptions.ControllerExceptionHandler;
import br.fiap.hackathonpostech.application.gateway.ClienteGateway;
import br.fiap.hackathonpostech.application.usecase.CartaoUseCase;
import br.fiap.hackathonpostech.application.usecase.ClienteUseCase;
import br.fiap.hackathonpostech.domain.entity.Cartao;
import br.fiap.hackathonpostech.domain.entity.Cliente;
import br.fiap.hackathonpostech.infra.adpter.CartaoRepositoryGateway;
import br.fiap.hackathonpostech.infra.mapper.CartaoMapper;
import br.fiap.hackathonpostech.infra.persistence.repository.CartaoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CartaoControllerTest {
    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    MockMvc mockMvc;
    AutoCloseable mock;

    @Mock
    private CartaoRepository cartaoRepository;
    @Mock
    private ClienteGateway clienteGateway;
    private ClienteUseCase clienteUseCase;


    @BeforeEach
    public void setup() {
        mock = MockitoAnnotations.openMocks(this);

        clienteUseCase = new ClienteUseCase(clienteGateway);

        CartaoUseCase cartaoUseCase = new CartaoUseCase(
                new CartaoRepositoryGateway(cartaoRepository), clienteUseCase);
        CartaoController cartaoController = new CartaoController(cartaoUseCase);

        mockMvc = MockMvcBuilders.standaloneSetup(cartaoController)
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

    @Nested
    class GerarCartao{
        @Test
        void deveGerarCartao() throws Exception {
            CartaoRequest cartaoRequest = gerarCartaoRequest();
            Cartao cartao = CartaoMapper.requestToCartao(cartaoRequest);

            when(clienteGateway.buscarClientePorCpf(cartao.getCpf())).thenReturn(gerarClienteRequest(cartao.getCpf()));

            mockMvc.perform(post("/cartao")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(cartaoRequest)))
                    .andExpect(status().isOk());

        }
    }

    private CartaoRequest gerarCartaoRequest() {
        return new CartaoRequest(
                "11111111111",
                1000,
                "1111111111111111",
                "01/28",
                "11"
        );
    }

    private Cliente gerarClienteRequest(String cpf) {
        return Cliente.builder()
                .id(1)
                .cpf(cpf)
                .build();
    }

    private static String asJsonString(Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    private <T> T jsonToObject(String json, Class<T> classz) throws JsonProcessingException {
        return MAPPER.readValue(json, classz);
    }

    private <T> T jsonToObject(String json, TypeReference<T> classz) throws JsonProcessingException {
        return MAPPER.readerFor(classz).readValue(json);
    }
}
