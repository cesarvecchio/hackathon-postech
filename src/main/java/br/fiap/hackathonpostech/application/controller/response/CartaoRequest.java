package br.fiap.hackathonpostech.application.controller.response;

public record CartaoRequest(
        String cpf,
        Integer limite,
        String numero,
        String dataValidade,
        String cvv
) {
}
