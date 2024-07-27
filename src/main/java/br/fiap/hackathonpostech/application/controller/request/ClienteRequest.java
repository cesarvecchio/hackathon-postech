package br.fiap.hackathonpostech.application.controller.request;

public record ClienteRequest(
    String cpf,
    String nome,
    String email,
    String telefone,
    String rua,
    String cidade,
    String estado,
    String cep,
    String pais
) {}
