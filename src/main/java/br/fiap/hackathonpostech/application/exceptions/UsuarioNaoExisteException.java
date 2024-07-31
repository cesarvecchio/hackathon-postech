package br.fiap.hackathonpostech.application.exceptions;

public class UsuarioNaoExisteException extends RuntimeException{
    public UsuarioNaoExisteException(String message) {
        super(message);
    }
}
