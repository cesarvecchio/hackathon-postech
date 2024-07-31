package br.fiap.hackathonpostech.application.exceptions;

public class CartaoNaoExisteException extends RuntimeException {
    public CartaoNaoExisteException(String message) {
        super(message);
    }
}
