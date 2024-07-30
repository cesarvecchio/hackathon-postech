package br.fiap.hackathonpostech.domain.entity;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cliente {
    private UUID id;
    private String cpf;
    private String nome;
    private String email;
    private String telefone;
    private String rua;
    private String cidade;
    private String estado;
    private String cep;
    private String pais;
}
