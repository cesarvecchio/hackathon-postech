package br.fiap.hackathonpostech.domain.entity;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cartao {
    private UUID id;
    private String cpf;
    private Integer limite;
    private String numero;
    private String dataValidade;
    private String cvv;
}
