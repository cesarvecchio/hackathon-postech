package br.fiap.hackathonpostech.application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.fiap.hackathonpostech.application.controller.request.CartaoRequest;
import br.fiap.hackathonpostech.application.controller.response.CartaoResponse;
import br.fiap.hackathonpostech.application.exceptions.ControllerExceptionHandler;
import br.fiap.hackathonpostech.application.exceptions.StandardErrorException;
import br.fiap.hackathonpostech.application.gateway.ClienteGateway;
import br.fiap.hackathonpostech.application.usecase.CartaoUseCase;
import br.fiap.hackathonpostech.application.usecase.ClienteUseCase;
import br.fiap.hackathonpostech.domain.entity.Cartao;
import br.fiap.hackathonpostech.domain.entity.Cliente;
import br.fiap.hackathonpostech.infra.adpter.CartaoRepositoryGateway;
import br.fiap.hackathonpostech.infra.mapper.CartaoMapper;
import br.fiap.hackathonpostech.infra.persistence.entity.CartaoEntity;
import br.fiap.hackathonpostech.infra.persistence.repository.CartaoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SuppressWarnings("unchecked")
public class CartaoControllerTest {
    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    MockMvc mockMvc;
    AutoCloseable mock;

    @Mock
    private CartaoRepository cartaoRepository;
    @Mock
    private ClienteGateway clienteGateway;

    @BeforeEach
    public void setup() {
        mock = MockitoAnnotations.openMocks(this);

        ClienteUseCase clienteUseCase = new ClienteUseCase(clienteGateway);

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
    class GerarCartao {
        @Nested
        class DeveGerar {
            @Test
            void deveGerarCartao() throws Exception {
                CartaoRequest cartaoRequest = gerarCartaoRequest();
                Cartao cartao = CartaoMapper.requestToCartao(cartaoRequest);

                when(clienteGateway.buscarClientePorCpf(cartao.getCpf())).thenReturn(gerarClienteRequest(cartao.getCpf()));
                when(cartaoRepository.findAllByCpf(cartao.getCpf())).thenReturn(List.of(cartaoEntityList().get(1)));
                when(cartaoRepository.existsByNumero(cartao.getNumero())).thenReturn(false);

                mockMvc.perform(post("/cartao")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(cartaoRequest)))
                        .andExpect(status().isOk());

                verify(clienteGateway).buscarClientePorCpf(cartao.getCpf());
                verify(cartaoRepository).findAllByCpf(cartao.getCpf());
                verify(cartaoRepository).existsByNumero(cartao.getNumero());
            }

            @Test
            void deveGerarExcecaoClienteNaoExiste() throws Exception {
                CartaoRequest cartaoRequest = gerarCartaoRequest();
                Cartao cartao = CartaoMapper.requestToCartao(cartaoRequest);

                when(clienteGateway.buscarClientePorCpf(cartao.getCpf())).thenReturn(null);

                mockMvc.perform(post("/cartao")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(cartaoRequest)))
                        .andExpect(status().isInternalServerError())
                        .andExpect(result -> {
                            StandardErrorException validationErrors = jsonToObject(result.getResponse().getContentAsString(),
                                    StandardErrorException.class);

                            assertEquals("Cliente nao existe exception", validationErrors.getError());
                            assertEquals("Nenhum cliente cadastrado com o CPF informado.", validationErrors.getMessage());
                        });

                verify(clienteGateway).buscarClientePorCpf(cartao.getCpf());
                verify(cartaoRepository, times(0)).findAllByCpf(cartao.getCpf());
                verify(cartaoRepository, times(0)).existsByNumero(cartao.getNumero());
            }

            @Test
            void deveGerarExcecaoLimiteQtdCartao() throws Exception {
                CartaoRequest cartaoRequest = gerarCartaoRequest();
                Cartao cartao = CartaoMapper.requestToCartao(cartaoRequest);

                when(clienteGateway.buscarClientePorCpf(cartao.getCpf())).thenReturn(gerarClienteRequest(cartao.getCpf()));
                when(cartaoRepository.findAllByCpf(cartao.getCpf())).thenReturn(cartaoEntityList());

                mockMvc.perform(post("/cartao")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(cartaoRequest)))
                        .andExpect(status().isForbidden())
                        .andExpect(result -> {
                            StandardErrorException validationErrors = jsonToObject(result.getResponse().getContentAsString(),
                                    StandardErrorException.class);

                            assertEquals("Limite qtd cartoes exception", validationErrors.getError());
                            assertEquals("Limite da quantide de cartoes por CPF excedido!", validationErrors.getMessage());
                        });
                verify(clienteGateway).buscarClientePorCpf(cartao.getCpf());
                verify(cartaoRepository).findAllByCpf(cartao.getCpf());
                verify(cartaoRepository, times(0)).existsByNumero(cartao.getNumero());
            }

            @Test
            void deveGerarExcecaoCartaoExiste() throws Exception {
                CartaoRequest cartaoRequest = gerarCartaoRequest();
                Cartao cartao = CartaoMapper.requestToCartao(cartaoRequest);

                when(clienteGateway.buscarClientePorCpf(cartao.getCpf())).thenReturn(gerarClienteRequest(cartao.getCpf()));
                when(cartaoRepository.findAllByCpf(cartao.getCpf())).thenReturn(List.of(cartaoEntityList().get(1)));
                when(cartaoRepository.existsByNumero(cartao.getNumero())).thenReturn(true);

                mockMvc.perform(post("/cartao")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(cartaoRequest)))
                        .andExpect(status().isInternalServerError())
                        .andExpect(result -> {
                            StandardErrorException validationErrors = jsonToObject(result.getResponse().getContentAsString(),
                                    StandardErrorException.class);

                            assertEquals("Cartao existe exception", validationErrors.getError());
                            assertEquals("Numero de cartão já existente.", validationErrors.getMessage());
                        });

                verify(clienteGateway).buscarClientePorCpf(cartao.getCpf());
                verify(cartaoRepository).findAllByCpf(cartao.getCpf());
                verify(cartaoRepository).existsByNumero(cartao.getNumero());
            }
        }

        @Nested
        class BuscarCartoesPorCpf {
            @Test
            void deveBuscarCartoesPorCpf() throws Exception {
                String cpf = "11111111111";

                when(cartaoRepository.findAllByCpf(cpf)).thenReturn(cartaoEntityList());

                mockMvc.perform(get("/cartao/{cpf}", cpf)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(
                                result -> {
                                    List<CartaoResponse> respose = new ArrayList<CartaoResponse>(
                                            jsonToObject(result.getResponse().getContentAsString(),
                                            List.class));

                                    assertEquals(2, respose.size());
                                }
                        );

                verify(cartaoRepository).findAllByCpf(cpf);
            }
        }
    }

    private List<CartaoEntity> cartaoEntityList(){
        return List.of(
                new CartaoEntity(
                        UUID.randomUUID(),
                        "11111111111",
                        1000.00,
                        "1111111111111111",
                        "01/28",
                        "11"
                ),
                new CartaoEntity(
                        UUID.randomUUID(),
                        "11111111111",
                        1000.00,
                        "2222222222222222",
                        "01/28",
                        "11"
                )
        );
    }

    private CartaoRequest gerarCartaoRequest() {
        return new CartaoRequest(
                "11111111111",
                1000.00,
                "1111111111111111",
                "01/28",
                "11"
        );
    }

    private Cliente gerarClienteRequest(String cpf) {
        return Cliente.builder()
            .id(UUID.randomUUID())
            .cpf(cpf)
            .build();
    }

    private static String asJsonString(Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    private <T> T jsonToObject(String json, Class<T> classz) throws JsonProcessingException {
        return MAPPER.readValue(json, classz);
    }
}
