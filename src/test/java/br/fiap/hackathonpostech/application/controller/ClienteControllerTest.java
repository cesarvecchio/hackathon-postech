package br.fiap.hackathonpostech.application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.fiap.hackathonpostech.application.controller.request.ClienteRequest;
import br.fiap.hackathonpostech.application.controller.response.ClienteResponse;
import br.fiap.hackathonpostech.application.exceptions.ControllerExceptionHandler;
import br.fiap.hackathonpostech.application.exceptions.StandardErrorException;
import br.fiap.hackathonpostech.application.usecase.ClienteUseCase;
import br.fiap.hackathonpostech.application.validatons.ValidationError;
import br.fiap.hackathonpostech.domain.entity.Cliente;
import br.fiap.hackathonpostech.infra.adpter.ClienteRepositoryGateway;
import br.fiap.hackathonpostech.infra.mapper.ClienteMapper;
import br.fiap.hackathonpostech.infra.persistence.entity.ClienteEntity;
import br.fiap.hackathonpostech.infra.persistence.repository.ClienteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ClienteControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    MockMvc mockMvc;

    AutoCloseable mock;

    @Mock
    private ClienteRepository clienteRepository;

    @BeforeEach
    public void setup() {
        mock = MockitoAnnotations.openMocks(this);
        ClienteUseCase clienteUseCase = new ClienteUseCase(new ClienteRepositoryGateway(clienteRepository));
        ClienteController clienteController = new ClienteController(clienteUseCase);

        mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
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
    void registrarCliente() throws Exception {
        //Arrange
        ClienteRequest clienteRequest = gerarClienteRequest("12345678901");
        Cliente cliente = ClienteMapper.requestToCliente(clienteRequest);
        ClienteEntity clienteEntity = ClienteMapper.clienteToEntity(cliente);
        ClienteEntity clienteEntitySalvo = ClienteMapper.clienteToEntity(cliente);
        clienteEntitySalvo.setId(1);

        when(clienteRepository.save(clienteEntity)).thenReturn(clienteEntitySalvo);

        //Act && Assert
        mockMvc.perform(post("/cliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(clienteRequest)))
            .andExpect(status().isOk())
            .andExpect(result -> {
                ClienteResponse clienteResponse = jsonToObject(result.getResponse().getContentAsString(), ClienteResponse.class);
                assertEquals(clienteEntitySalvo.getId().toString(), clienteResponse.id_cliente());
            });

        verify(clienteRepository, times(1)).save(clienteEntity);
    }

    @Test
    void deveGerarExcecaoQuandoCpfNull() throws Exception {
        //Arrange
        ClienteRequest clienteRequest = gerarClienteRequest(null);

        //Act && Assert
        mockMvc.perform(post("/cliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(clienteRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(result -> {
                List<ValidationError> validationErrors = jsonToObject(result.getResponse().getContentAsString(), new TypeReference<>() {});
                assertEquals(validationErrors.get(0).field(), "cpf");
                assertEquals(validationErrors.get(0).errorMessage(), "must not be null");
            });
    }

    @Test
    void deveGerarExcecaoQuandoJaExisteCpfRegistrado() throws Exception {
        //Arrange
        ClienteRequest clienteRequest = gerarClienteRequest("12345678901");
        Cliente cliente = ClienteMapper.requestToCliente(clienteRequest);
        ClienteEntity clienteEntity = ClienteMapper.clienteToEntity(cliente);

        when(clienteRepository.findByCpf(cliente.getCpf())).thenReturn(Optional.of(clienteEntity));

        //Act && Assert
        mockMvc.perform(post("/cliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(clienteRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(result -> {
                StandardErrorException validationErrors = jsonToObject(result.getResponse().getContentAsString(), StandardErrorException.class);
                assertEquals(validationErrors.getError(), "Cliente existe exception");
                assertEquals(validationErrors.getMessage(), "Já existe um cliente com o CPF informado.");
            });
    }

    private ClienteRequest gerarClienteRequest(String cpf) {
        return new ClienteRequest(cpf, "12345678", "", "", "", "", "", "", "");
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
