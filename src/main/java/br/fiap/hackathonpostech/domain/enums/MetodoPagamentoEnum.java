package br.fiap.hackathonpostech.domain.enums;

import lombok.Getter;

@Getter
public enum MetodoPagamentoEnum {

    CARTAO_CREDITO("cartao_credito");

    private final String valor;

    MetodoPagamentoEnum(String valor) {
        this.valor = valor;
    }

    public static MetodoPagamentoEnum fromValor(String valor) {
        for (MetodoPagamentoEnum metodo : MetodoPagamentoEnum.values()) {
            if (metodo.getValor().equals(valor)) {
                return metodo;
            }
        }
        throw new IllegalArgumentException("Valor desconhecido: " + valor);
    }
}
