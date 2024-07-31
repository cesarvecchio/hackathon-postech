package br.fiap.hackathonpostech.domain.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {

    APROVADO("aprovado"),
    REJEITADO("rejeitado");

    private final String valor;

    StatusEnum(String valor) {
        this.valor = valor;
    }

    public static StatusEnum fromValor(String valor) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getValor().equals(valor)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Valor desconhecido: " + valor);
    }
}
