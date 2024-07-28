package br.fiap.hackathonpostech.application.exceptions;

public class CartaoExisteException extends RuntimeException {
    public CartaoExisteException(String message) {
        super(message);
    }
}
