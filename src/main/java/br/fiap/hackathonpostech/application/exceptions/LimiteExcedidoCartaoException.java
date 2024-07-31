package br.fiap.hackathonpostech.application.exceptions;

public class LimiteExcedidoCartaoException extends RuntimeException {
    public LimiteExcedidoCartaoException(String message) {
        super(message);
    }
}
