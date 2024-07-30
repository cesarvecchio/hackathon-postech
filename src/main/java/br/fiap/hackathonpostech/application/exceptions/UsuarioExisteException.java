package br.fiap.hackathonpostech.application.exceptions;

public class UsuarioExisteException extends RuntimeException{
    public UsuarioExisteException(String message) {
        super(message);
    }
}
