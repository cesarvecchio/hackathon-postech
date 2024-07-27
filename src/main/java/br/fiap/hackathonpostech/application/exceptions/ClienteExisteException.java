package br.fiap.hackathonpostech.application.exceptions;

public class ClienteExisteException extends RuntimeException {

    public ClienteExisteException(String message) {
        super(message);
    }
}
