package br.fiap.hackathonpostech.application.exceptions;

public class ClienteExisteException extends RuntimeException {

    public ClienteExisteException(String message) {
        super(message);
    }

    public ClienteExisteException(String message, Throwable cause) {
        super(message, cause);
    }
}
