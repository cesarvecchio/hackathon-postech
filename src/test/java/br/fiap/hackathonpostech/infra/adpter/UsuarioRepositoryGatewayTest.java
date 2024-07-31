package br.fiap.hackathonpostech.infra.adpter;

import br.fiap.hackathonpostech.infra.mapper.UsuarioMapper;
import br.fiap.hackathonpostech.infra.persistence.entity.UsuarioEntity;
import br.fiap.hackathonpostech.infra.persistence.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UsuarioRepositoryGatewayTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private UsuarioRepositoryGateway usuarioRepositoryGateway;

    AutoCloseable mock;
    @BeforeEach
    public void setup() {
        mock = MockitoAnnotations.openMocks(this);

        usuarioRepositoryGateway = new UsuarioRepositoryGateway(usuarioRepository);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void deveEncontrarPorUsuario(){
        var usuarioEntity = usuarioEntity();
        var usuario = UsuarioMapper.entityToUsuario(usuarioEntity);

        when(usuarioRepository.findByUsuario(usuarioEntity.getUsuario())).thenReturn(Optional.of(usuarioEntity));

        var resultado = usuarioRepositoryGateway.encontrarPorUsuario(usuarioEntity.getUsuario());

        assertEquals(usuario, resultado);

        verify(usuarioRepository).findByUsuario(usuario.getUsuario());
    }

    private UsuarioEntity usuarioEntity(){
        return new UsuarioEntity("usuario", "senha");
    }
}
