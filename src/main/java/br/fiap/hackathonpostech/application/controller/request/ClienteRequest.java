package br.fiap.hackathonpostech.application.controller.request;

import jakarta.validation.constraints.NotNull;

public record ClienteRequest(
    @NotNull String cpf,
    String nome,
    String email,
    String telefone,
    String rua,
    String cidade,
    String estado,
    String cep,
    String pais
) {}
