package br.fiap.hackathonpostech.application.exceptions;

public class LimiteQtdCartoesException extends RuntimeException {
    public LimiteQtdCartoesException(String message) {
        super(message);
    }
}
