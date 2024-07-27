package br.fiap.hackathonpostech.domain.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cliente {
    private Integer id;
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
