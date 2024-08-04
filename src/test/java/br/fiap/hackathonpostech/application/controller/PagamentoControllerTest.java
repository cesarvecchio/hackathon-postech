package br.fiap.hackathonpostech.application.controller;

import br.fiap.hackathonpostech.application.controller.request.PagamentoRequest;
import br.fiap.hackathonpostech.application.controller.response.PagamentoListResponse;
import br.fiap.hackathonpostech.application.exceptions.ControllerExceptionHandler;
import br.fiap.hackathonpostech.application.exceptions.StandardErrorException;
import br.fiap.hackathonpostech.application.gateway.CartaoGateway;
import br.fiap.hackathonpostech.application.gateway.ClienteGateway;
import br.fiap.hackathonpostech.application.usecase.CartaoUseCase;
import br.fiap.hackathonpostech.application.usecase.ClienteUseCase;
import br.fiap.hackathonpostech.application.usecase.PagamentoUseCase;
import br.fiap.hackathonpostech.domain.entity.Cartao;
import br.fiap.hackathonpostech.domain.entity.Cliente;
import br.fiap.hackathonpostech.domain.entity.Pagamento;
import br.fiap.hackathonpostech.infra.adpter.PagamentoRepositoryGateway;
import br.fiap.hackathonpostech.infra.mapper.PagamentoMapper;
import br.fiap.hackathonpostech.infra.persistence.entity.PagamentoEntity;
import br.fiap.hackathonpostech.infra.persistence.repository.PagamentoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static br.fiap.hackathonpostech.domain.enums.MetodoPagamentoEnum.CARTAO_CREDITO;
import static br.fiap.hackathonpostech.domain.enums.StatusEnum.APROVADO;
import static br.fiap.hackathonpostech.domain.enums.StatusEnum.REJEITADO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PagamentoControllerTest {
    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    MockMvc mockMvc;
    AutoCloseable mock;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private CartaoGateway cartaoGateway;

    @Mock
    private ClienteGateway clienteGateway;

    @BeforeEach
    public void setup() {
        mock = MockitoAnnotations.openMocks(this);
        ClienteUseCase clienteUseCase = new ClienteUseCase(clienteGateway);
        CartaoUseCase cartaoUseCase = new CartaoUseCase(cartaoGateway, clienteUseCase);

        PagamentoUseCase pagamentoUseCase = new PagamentoUseCase(
                new PagamentoRepositoryGateway(pagamentoRepository), cartaoUseCase, clienteUseCase);

        PagamentoController pagamentoController = new PagamentoController(pagamentoUseCase);

        mockMvc = MockMvcBuilders.standaloneSetup(pagamentoController)
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
    class RegistrarPagamento {

        @Test
        void deveRegistrarPagamento() throws Exception {
            PagamentoRequest pagamentoRequest = gerarPagamentoRequest();
            Pagamento pagamento = PagamentoMapper.requestToPagamento(pagamentoRequest);
            List<Cartao> cartoes = gerarCartaoList();

            when(cartaoGateway.buscarCartoesPorCpf(pagamento.getCpf())).thenReturn(cartoes);
            when(pagamentoRepository.save(any(PagamentoEntity.class))).thenReturn(gerarPagamentoEntity());
            when(cartaoGateway.buscarCartaoPorId(cartoes.get(0).getId())).thenReturn(Optional.ofNullable(cartoes.get(0)));
            when(clienteGateway.buscarClientePorCpf(pagamento.getCpf())).thenReturn(gerarClienteRequest("11111111111"));

            mockMvc.perform(post("/pagamentos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(pagamentoRequest)))
                    .andExpect(status().isOk());

            verify(pagamentoRepository, times(1)).save(any(PagamentoEntity.class));
        }

        @Test
        void deveGerarExcecaoQuandoCodigoCartaoInvalidoRegistrarPagamento() throws Exception {
            PagamentoRequest pagamentoRequest = new PagamentoRequest("11111111111",
                    "1111111111111111",
                    "11/28",
                    "123",
                    100.00
            );
            Pagamento pagamento = PagamentoMapper.requestToPagamento(pagamentoRequest);
            List<Cartao> cartoes = gerarCartaoList();

            when(cartaoGateway.buscarCartoesPorCpf(pagamento.getCpf())).thenReturn(cartoes);

            mockMvc.perform(post("/pagamentos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(pagamentoRequest)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(result -> {
                        StandardErrorException validationErrors = jsonToObject(result.getResponse().getContentAsString(),
                                StandardErrorException.class);
                        assertEquals("Código cartão inválido exception", validationErrors.getError());
                        assertEquals("Código informado inválido!", validationErrors.getMessage());
                    });

            verify(pagamentoRepository, times(0)).save(any(PagamentoEntity.class));
        }

        @Test
        void deveGerarExcecaoQuandoDataValidadeIncorretaRegistrarPagamento() throws Exception {
            PagamentoRequest pagamentoRequest = new PagamentoRequest("11111111111",
                    "1111111111111111",
                    "05/28",
                    "111",
                    100.00
            );
            Pagamento pagamento = PagamentoMapper.requestToPagamento(pagamentoRequest);
            List<Cartao> cartoes = gerarCartaoList();

            when(cartaoGateway.buscarCartoesPorCpf(pagamento.getCpf())).thenReturn(cartoes);

            mockMvc.perform(post("/pagamentos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(pagamentoRequest)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(result -> {
                        StandardErrorException validationErrors = jsonToObject(result.getResponse().getContentAsString(),
                                StandardErrorException.class);
                        assertEquals("Validade cartão exception", validationErrors.getError());
                        assertEquals("Data de validade do cartão incorreta!", validationErrors.getMessage());
                    });

            verify(pagamentoRepository, times(0)).save(any(PagamentoEntity.class));
        }

        @Test
        void deveGerarExcecaoQuandoDataValidadeExpiradaRegistrarPagamento() throws Exception {
            PagamentoRequest pagamentoRequest = new PagamentoRequest("11111111111",
                    "1111111111111111",
                    "12/23",
                    "111",
                    100.00
            );
            Pagamento pagamento = PagamentoMapper.requestToPagamento(pagamentoRequest);
            List<Cartao> cartoes = gerarCartaoList();
            cartoes.get(0).setDataValidade("12/23");

            when(cartaoGateway.buscarCartoesPorCpf(pagamento.getCpf())).thenReturn(cartoes);

            mockMvc.perform(post("/pagamentos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(pagamentoRequest)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(result -> {
                        StandardErrorException validationErrors = jsonToObject(result.getResponse().getContentAsString(),
                                StandardErrorException.class);
                        assertEquals("Validade cartão exception", validationErrors.getError());
                        assertEquals("Data de validade do cartão expirada!", validationErrors.getMessage());
                    });

            verify(pagamentoRepository, times(0)).save(any(PagamentoEntity.class));
        }

        @Test
        void deveGerarExcecaoQuandoDataValidadeFormatoInvalidoRegistrarPagamento() throws Exception {
            PagamentoRequest pagamentoRequest = new PagamentoRequest("11111111111",
                    "1111111111111111",
                    "99/99",
                    "111",
                    100.00
            );
            Pagamento pagamento = PagamentoMapper.requestToPagamento(pagamentoRequest);
            List<Cartao> cartoes = gerarCartaoList();
            cartoes.get(0).setDataValidade("99/99");

            when(cartaoGateway.buscarCartoesPorCpf(pagamento.getCpf())).thenReturn(cartoes);

            mockMvc.perform(post("/pagamentos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(pagamentoRequest)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(result -> {
                        StandardErrorException validationErrors = jsonToObject(result.getResponse().getContentAsString(),
                                StandardErrorException.class);
                        assertEquals("Validade cartão exception", validationErrors.getError());
                        assertEquals("Formato de data inválido. Deve ser MM/yy.", validationErrors.getMessage());
                    });

            verify(pagamentoRepository, times(0)).save(any(PagamentoEntity.class));
        }

        @Test
        void deveGerarExcecaoQuandoLimiteDisponivelInsuficienteRegistrarPagamento() throws Exception {
            PagamentoRequest pagamentoRequest = new PagamentoRequest("11111111111",
                    "1111111111111111",
                    "11/28",
                    "111",
                    2500.00
            );
            Pagamento pagamento = PagamentoMapper.requestToPagamento(pagamentoRequest);
            List<Cartao> cartoes = gerarCartaoList();

            when(cartaoGateway.buscarCartoesPorCpf(pagamento.getCpf())).thenReturn(cartoes);
            when(pagamentoRepository.save(any(PagamentoEntity.class))).thenReturn(gerarPagamentoEntity());

            mockMvc.perform(post("/pagamentos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(pagamentoRequest)))
                    .andExpect(status().isPaymentRequired())
                    .andExpect(result -> {
                        StandardErrorException validationErrors = jsonToObject(result.getResponse().getContentAsString(),
                                StandardErrorException.class);
                        assertEquals("Limite Execedido exception", validationErrors.getError());
                        assertEquals("Limite disponível insuficiente para realizar o pagamento!", validationErrors.getMessage());
                    });

            verify(pagamentoRepository, times(1)).save(any(PagamentoEntity.class));
        }
    }

    @Nested
    class ConsultarPagamentoPorCliente {

        @Test
        void deveRetornarPagamentos() throws Exception {
            Cliente cliente = gerarClienteRequest("11111111111");
            List<Cartao> cartoes = gerarCartaoList();
            List<PagamentoListResponse> pagamentoListResponseList = gerarPagamentoListResponseList();

            List<PagamentoEntity> pagamentoList = gerarPagamentoList().stream()
                    .map(PagamentoMapper::pagamentoToEntity)
                    .collect(Collectors.toList());

            when(clienteGateway.buscarClientePorId(cliente.getId())).thenReturn(cliente);
            when(cartaoGateway.buscarCartoesPorCpf(cliente.getCpf())).thenReturn(cartoes);
            when(pagamentoRepository.findByCartaoIdIn(cartoes.stream().map(Cartao::getId).toList())).thenReturn(pagamentoList);

            mockMvc.perform(get("/pagamentos/{chave}", cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(pagamentoListResponseList)))
                    .andExpect(status().isOk());

            verify(clienteGateway).buscarClientePorId(cliente.getId());
            verify(cartaoGateway).buscarCartoesPorCpf(cliente.getCpf());
            verify(pagamentoRepository).findByCartaoIdIn(cartoes.stream().map(Cartao::getId).toList());
        }

        @Test
        void deveGerarExcecaoQuandoNaoEncontrarCliente() throws Exception {
            Cliente cliente = gerarClienteRequest("11111111111");

            when(clienteGateway.buscarClientePorId(cliente.getId())).thenReturn(null);

            mockMvc.perform(get("/pagamentos/{chave}", cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(result -> {
                        StandardErrorException validationErrors = jsonToObject(result.getResponse().getContentAsString(),
                                StandardErrorException.class);

                        assertEquals("Cliente nao existe exception", validationErrors.getError());
                        assertEquals("Cliente inexistente", validationErrors.getMessage());
                    });

            verify(clienteGateway).buscarClientePorId(cliente.getId());
        }

        @Test
        void deveGerarExcecaoQuandoNaoEncontrarCartao() throws Exception {
            Cliente cliente = gerarClienteRequest("11111111111");

            when(clienteGateway.buscarClientePorId(cliente.getId())).thenReturn(cliente);
            when(cartaoGateway.buscarCartoesPorCpf(cliente.getCpf())).thenReturn(null);

            mockMvc.perform(get("/pagamentos/{chave}", cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(result -> {
                        StandardErrorException validationErrors = jsonToObject(result.getResponse().getContentAsString(),
                                StandardErrorException.class);
                        assertEquals("Cartão não existe exception", validationErrors.getError());
                        assertEquals("Nenhum cartão encontrado para o cliente", validationErrors.getMessage());
                    });

            verify(clienteGateway).buscarClientePorId(cliente.getId());
            verify(cartaoGateway).buscarCartoesPorCpf(cliente.getCpf());
        }

        @Test
        void deveGerarExcecaoQuandoNaoEncontrarPagamentos() throws Exception {
            Cliente cliente = gerarClienteRequest("11111111111");
            List<Cartao> cartoes = gerarCartaoList();

            when(clienteGateway.buscarClientePorId(cliente.getId())).thenReturn(cliente);
            when(cartaoGateway.buscarCartoesPorCpf(cliente.getCpf())).thenReturn(cartoes);
            when(pagamentoRepository.findByCartaoIdIn(cartoes.stream().map(Cartao::getId).toList())).thenReturn(List.of());

            mockMvc.perform(get("/pagamentos/{chave}", cliente.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(result -> {
                        StandardErrorException validationErrors = jsonToObject(result.getResponse().getContentAsString(),
                                StandardErrorException.class);
                        assertEquals("Pagamento não encontrado exception", validationErrors.getError());
                        assertEquals("Nenhum pagamento encontrado para o cliente", validationErrors.getMessage());
                    });

            verify(clienteGateway).buscarClientePorId(cliente.getId());
            verify(cartaoGateway).buscarCartoesPorCpf(cliente.getCpf());
            verify(pagamentoRepository).findByCartaoIdIn(cartoes.stream().map(Cartao::getId).toList());
        }
    }

    private List<Cartao> gerarCartaoList() {
        return List.of(
                Cartao.builder()
                        .id(UUID.randomUUID())
                        .cpf("11111111111")
                        .cvv("111")
                        .dataValidade("11/28")
                        .limite(1000.00)
                        .numero("1111111111111111")
                        .build(),
                Cartao.builder()
                        .id(UUID.randomUUID())
                        .cpf("11111111111")
                        .cvv("111")
                        .dataValidade("11/28")
                        .limite(1000.00)
                        .build()
        );
    }

    private Cliente gerarClienteRequest(String cpf) {
        return Cliente.builder()
                .id(UUID.randomUUID())
                .cpf(cpf)
                .build();
    }

    private List<PagamentoListResponse> gerarPagamentoListResponseList() {
        return List.of(
                new PagamentoListResponse(
                        100.00,
                        "COMPRA X",
                        "CARTAO_CREDITO",
                        "APROVADO"
                ),
                new PagamentoListResponse(
                        100.00,
                        "COMPRA X",
                        "CARTAO_CREDITO",
                        "APROVADO"
                ),
                new PagamentoListResponse(
                        10000.00,
                        "COMPRA X",
                        "CARTAO_CREDITO",
                        "REJEITADO"
                )
        );
    }

    private List<Pagamento> gerarPagamentoList() {
        return List.of(
                Pagamento.builder()
                        .id(UUID.randomUUID())
                        .valor(100.00)
                        .descricao("COMPRA X")
                        .metodoPagamento(CARTAO_CREDITO)
                        .status(APROVADO)
                        .cpf("11111111111")
                        .numero("1111111111111111")
                        .dataValidade("11/28")
                        .cvv("111")
                        .build(),

                Pagamento.builder()
                        .id(UUID.randomUUID())
                        .valor(100000.00)
                        .descricao("COMPRA X")
                        .metodoPagamento(CARTAO_CREDITO)
                        .status(REJEITADO)
                        .cpf("11111111111")
                        .numero("1111111111111111")
                        .dataValidade("11/28")
                        .cvv("111")
                        .build()
        );
    }

    private PagamentoRequest gerarPagamentoRequest() {
        return new PagamentoRequest("11111111111",
                "1111111111111111",
                "11/28",
                "111",
                100.00
        );
    }

    private PagamentoEntity gerarPagamentoEntity() {
        return new PagamentoEntity(
                UUID.randomUUID(),
                100.00,
                "Compra produdo X",
                CARTAO_CREDITO,
                APROVADO
        );
    }

    private static String asJsonString(Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    private <T> T jsonToObject(String json, Class<T> classz) throws JsonProcessingException {
        return MAPPER.readValue(json, classz);
    }
}
