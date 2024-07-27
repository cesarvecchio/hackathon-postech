package br.fiap.hackathonpostech.application.controller.request;

import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(
    @NotBlank String cpf,
    String nome,
    String email,
    String telefone,
    String rua,
    String cidade,
    String estado,
    String cep,
    String pais
) {}
