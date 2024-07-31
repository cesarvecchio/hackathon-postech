package br.fiap.hackathonpostech.application.exceptions;

public class CodigoCartaoInvalidoException extends RuntimeException {
    public CodigoCartaoInvalidoException(String message) {
        super(message);
    }
}
