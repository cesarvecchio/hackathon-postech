package br.fiap.hackathonpostech.application.controller.response;

public record PagamentoListResponse(
    Double valor,
    String descricao,
    String metodoPagamento,
    String status
) {
}
