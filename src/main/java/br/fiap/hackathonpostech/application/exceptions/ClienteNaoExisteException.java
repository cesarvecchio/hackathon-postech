package br.fiap.hackathonpostech.application.exceptions;

public class ClienteNaoExisteException extends RuntimeException {
    public ClienteNaoExisteException(String message) {
        super(message);
    }
}
