package br.fiap.hackathonpostech.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Cartao {
    private Integer id;
    private String cpf;
    private Integer limite;
    private String numero;
    private String dataValidade;
    private String cvv;
}
